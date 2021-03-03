java -Djava.security.debug=access,connect,resolve  -Djava.security.policy=server.policy -Djava.rmi.server.hostname=127.0.0.1 -Djava.class.path=../out/classes server.BankServer 
