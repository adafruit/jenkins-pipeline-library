def call(String platform) {

  new File("${pwd()}/examples").eachDirRecurse() { dir ->

    dir.eachFileMatch(~/.*.ino/) { file ->
      sh "arduino --board \$${platform} --verify ${file.getPath()} 2>&1"
    }

  }

}
