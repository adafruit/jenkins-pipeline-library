def call(target, version) {

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
  export PATH=\"\${BUILD_FOLDER}/arduino_ide:\${PATH}\"
  """

}
