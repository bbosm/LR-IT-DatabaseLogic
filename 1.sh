#!/bin/bash

#tnameserv -ORBInitialPort 8080

cd build/classes/main
rmic -iiop transfer.ServerImpl


