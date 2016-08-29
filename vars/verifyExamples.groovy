def call(String platform) {

  sh """#!/bin/bash
  arduino --board \$${platform} --save-prefs
  declare -a examples
  examples=(\$(find examples -name '*.ino'))
  for example in \"\${examples[@]}\"; do
    arduino --board \$${platform} --verify \${example} 2>&1
  done
  """

}
