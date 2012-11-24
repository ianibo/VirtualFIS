package vf

import org.apache.commons.lang.builder.HashCodeBuilder

class VFUserRole implements Serializable {

	VFUser vfUser
	VFRole vfRole

	boolean equals(other) {
		if (!(other instanceof VFUserRole)) {
			return false
		}

		other.vfUser?.id == vfUser?.id &&
			other.vfRole?.id == vfRole?.id
	}

	int hashCode() {
		def builder = new HashCodeBuilder()
		if (vfUser) builder.append(vfUser.id)
		if (vfRole) builder.append(vfRole.id)
		builder.toHashCode()
	}

	static VFUserRole get(long vfUserId, long vfRoleId) {
		find 'from VFUserRole where vfUser.id=:vfUserId and vfRole.id=:vfRoleId',
			[vfUserId: vfUserId, vfRoleId: vfRoleId]
	}

	static VFUserRole create(VFUser vfUser, VFRole vfRole, boolean flush = false) {
		new VFUserRole(vfUser: vfUser, vfRole: vfRole).save(flush: flush, insert: true)
	}

	static boolean remove(VFUser vfUser, VFRole vfRole, boolean flush = false) {
		VFUserRole instance = VFUserRole.findByvfUserAndvfRole(vfUser, vfRole)
		if (!instance) {
			return false
		}

		instance.delete(flush: flush)
		true
	}

	static void removeAll(VFUser vfUser) {
		executeUpdate 'DELETE FROM VFUserRole WHERE vfUser=:vfUser', [vfUser: vfUser]
	}

	static void removeAll(VFRole vfRole) {
		executeUpdate 'DELETE FROM VFUserRole WHERE vfRole=:vfRole', [vfRole: vfRole]
	}

	static mapping = {
		id composite: ['vfRole', 'vfUser']
		version false
	}
}
