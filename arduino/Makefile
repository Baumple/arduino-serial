compile:
	arduino-cli compile --fqbn arduino:avr:uno serial

flash: compile
	arduino-cli upload --fqbn arduino:avr:uno -p ${ARDUINO_PORT} serial


monitor:
	arduino-cli monitor --fqbn arduino:avr:uno -p ${ARDUINO_PORT}  -c baudrate=9600

flashmon: flash monitor

run:
	cd ../java/ && make run

all: flash run
