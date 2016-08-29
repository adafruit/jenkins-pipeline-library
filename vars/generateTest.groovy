call() {

  writeFile file: ".tests/arduino_serial.t", text: '''#!/usr/bin/env perl

  use warnings;
  use strict;

  use 5.10.0;

  use Getopt::Long;
  use Device::SerialPort;

  my $PORT = '/dev/ttyUSB0';
  GetOptions ("port=s" => \$PORT);

  my $port = Device::SerialPort->new($PORT);
  my $started = 0;
  my code = 0;

  $port->baudrate(115200);
  $port->databits(8);
  $port->parity('none');
  $port->stopbits(1);

  open(SERIAL, "+>$PORT");

  while (my $line = <SERIAL>) {

    if ($line eq '# Starting Tests.') {
      $started = 1;
    } elsif ($line eq '# Done with Tests.') {
       say $line;
       exit $code;
    }

    if (! $started) {
      next;
    }

    say $line;

    if ($line =~ /fail/i) {
      $code = 1;
    }

  }
  '''
}
