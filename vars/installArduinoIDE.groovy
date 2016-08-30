def call(target, version) {

  def path = sh(returnStdout: true, script: 'printf "%q\n" "$(pwd)/arduino_ide:${PATH}"').trim()

  sh """#!/bin/bash
  BUILD_FOLDER=`pwd`
  mkdir -p \$HOME/ide
  cd \$HOME/ide
  if [ ! -d \"arduino-${version}\" ]; then
    wget https://downloads.arduino.cc/arduino-${version}-linuxarm.tar.xz
    tar xf arduino-${version}-linuxarm.tar.xz
    mkdir arduino-${version}/portable
  fi
  cp -rf arduino-${version} \${BUILD_FOLDER}/arduino_ide
  """

  env.PATH = path

}
