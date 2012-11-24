package cxfisppsoap

class DepositService {

  def upload(file,authoritative,owner) {
    log.debug("upload: ${file}");
    log.debug("        ${authoritative}");
    log.debug("        ${owner}");
  }
}
