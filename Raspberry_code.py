import socket

import pigpio
import time

bufferSize  = 1024
sock = socket.socket(socket.AF_INET,socket.SOCK_DGRAM)      
udp_host = "0.0.0.0"	        
udp_port = 5000			                



servoYaw = pigpio.pi()
servoPitch=pigpio.pi()

servoYaw.set_servo_pulsewidth(14,1500)##pin 14 for  Yaw
servoPitch.set_servo_pulsewidth(15,1500)##pin 15 for pitch

sock.bind((udp_host,udp_port))
data,addr = sock.recvfrom(1024)
mydata=str(data)

MinPw=750
MaxPw=2200

def deg2pw(degree):
	return 750 + degree * 75.0/9.0##converting angle to pulsewidth




while True:
	
	data,addr = sock.recvfrom(1024)
	mydata=str(data)
	yawValueIndex=mydata.find('yaw')
	pitchValueIndex=mydata.find('pitch')
	YAW=mydata[yawValueIndex+4:yawValueIndex+8]
	PITCH=mydata[pitchValueIndex+6:pitchValueIndex+11]
	
	
	
	print (YAW +"  "+PITCH)
	PwYaw=deg2pw(YAW)
	PwPitch=deg2pw(PITCH)
        if PwYaw > MaxPw :
	    PwYaw=MaxPw
	if PwYaw<MinPw:
	    PwYaw=MinPw
	if PwPitch>MaxPw :
	    PwPitch=MaxPw
	if PwPitch < MinPw :
	    PWPitch=MinPw
	servoPitch.set_servo_pulsewidth(15,PwPitch)
    servoYaw.set_servo_pulsewidth(14,PwYaw)
	
