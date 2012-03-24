package com.k_int.vfis.auth

import org.apache.commons.lang.builder.HashCodeBuilder

class VfisPersonVfisAuthority implements Serializable {

	VfisPerson vfisPerson
	VfisAuthority vfisAuthority

	boolean equals(other) {
		if (!(other instanceof VfisPersonVfisAuthority)) {
			return false
		}

		other.vfisPerson?.id == vfisPerson?.id &&
			other.vfisAuthority?.id == vfisAuthority?.id
	}

	int hashCode() {
		def builder = new HashCodeBuilder()
		if (vfisPerson) builder.append(vfisPerson.id)
		if (vfisAuthority) builder.append(vfisAuthority.id)
		builder.toHashCode()
	}

	static VfisPersonVfisAuthority get(long vfisPersonId, long vfisAuthorityId) {
		find 'from VfisPersonVfisAuthority where vfisPerson.id=:vfisPersonId and vfisAuthority.id=:vfisAuthorityId',
			[vfisPersonId: vfisPersonId, vfisAuthorityId: vfisAuthorityId]
	}

	static VfisPersonVfisAuthority create(VfisPerson vfisPerson, VfisAuthority vfisAuthority, boolean flush = false) {
		new VfisPersonVfisAuthority(vfisPerson: vfisPerson, vfisAuthority: vfisAuthority).save(flush: flush, insert: true)
	}

	static boolean remove(VfisPerson vfisPerson, VfisAuthority vfisAuthority, boolean flush = false) {
		VfisPersonVfisAuthority instance = VfisPersonVfisAuthority.findByVfisPersonAndVfisAuthority(vfisPerson, vfisAuthority)
		if (!instance) {
			return false
		}

		instance.delete(flush: flush)
		true
	}

	static void removeAll(VfisPerson vfisPerson) {
		executeUpdate 'DELETE FROM VfisPersonVfisAuthority WHERE vfisPerson=:vfisPerson', [vfisPerson: vfisPerson]
	}

	static void removeAll(VfisAuthority vfisAuthority) {
		executeUpdate 'DELETE FROM VfisPersonVfisAuthority WHERE vfisAuthority=:vfisAuthority', [vfisAuthority: vfisAuthority]
	}

	static mapping = {
		id composite: ['vfisAuthority', 'vfisPerson']
		version false
	}
}
