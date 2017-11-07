#!/bin/bash

#tnameserv -ORBInitialPort 8080

cd build/classes/java/main
rmic -iiop transfer.ServerImpl


