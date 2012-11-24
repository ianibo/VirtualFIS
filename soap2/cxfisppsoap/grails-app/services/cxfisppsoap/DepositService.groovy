package cxfisppsoap

class DepositService {

  def upload(file,authoritative,owner,user) {
    log.debug("upload: file len=${file.length()}");
    log.debug("        ${authoritative}");
    log.debug("        ${owner}");
    log.debug("        ${user}");
  }
}
