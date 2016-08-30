def call(target, version) {

  sh """#!/bin/bash
  mkdir -p \$HOME/ide
  cd \$HOME/ide
  if[ ! -d \"arduino-${version}\" ]; then
    wget https://downloads.arduino.cc/arduino-${version}-linuxarm.tar.xz
    tar xf arduino-${version}-linuxarm.tar.xz
    mkdir arduino-${version}/portable
  fi
  cp -rf arduino-${version} \$WORKSPACE/arduino_ide
  """

}
