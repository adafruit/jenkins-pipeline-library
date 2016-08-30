def call() {

  writeFile file: ".tests/arduino_serial.t", text: '''#!/usr/bin/env node

  var SerialPort = require('serialport');
  var args = process.argv.slice(2);
  var started = false;
  var code = 0;

  setTimeout(function() {
    console.error('# test timeout');
    process.exit(1);
  }, 30000);

  var port = new SerialPort(args[0], {
    parser: SerialPort.parsers.readline('\r\n'),
    baudRate: 115200
  });

  port.on('error', function(err) {
    console.error(err);
    process.exit(1);
  });

  port.on('data', function(data) {

    if(/Starting Tests/i.test(data)) {
      started = true;
    } else if(/Done with Tests/i.test(data)) {
      console.log(data);
      process.exit(code);
    }

    if (! started) {
      return
    }

    console.log(data);

    if(/fail/i.test(data)) {
      code = 1;
    }

  });
  '''
}
