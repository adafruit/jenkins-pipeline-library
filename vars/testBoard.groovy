def call(String platform) {


  sh """#!/bin/bash
  arduino --board \$${platform} --save-prefs
  declare -a tests
  tests=(\$(find .tests -name '*.ino'))
  for test in \"\${tests[@]}\"; do
    arduino --board \$${platform} --port \$${platform}_PORT --upload $test 2>&1
    prove --formatter TAP::Formatter::Jenkins ./tests/arduino_serial.t :: --port \$${platform}_PORT
  done
  """

}
