compile:
	arduino-cli compile --fqbn arduino:avr:uno serial

flash: compile
	arduino-cli upload --fqbn arduino:avr:uno -p /dev/ttyACM1 serial


monitor:
	arduino-cli monitor --fqbn arduino:avr:uno -p /dev/ttyACM1  -c baudrate=9600
