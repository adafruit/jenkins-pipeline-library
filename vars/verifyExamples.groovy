def call(String platform) {

  sh """#!/bin/bash
  declare -a examples
  examples=(\$(find examples -name '*.ino'))
  for example in \"\${examples[@]}\"; do
    arduino --board \$${platform} --verify \${example} 2>&1
  done
  """

}
