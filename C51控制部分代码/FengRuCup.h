#ifndef __FENGRUCUP_H__
	#define __FENGRUCUP_H__

	#define int8 unsigned char
	#define int16 unsigned int

	extern int8 num[10];
	extern int8 sel[4];
	extern int8 get_cache[64];
	extern int8 top;
	void delay(int16);
	void show(int16);
	void get_init();
	void uart_init();
	void time_init();
	void send_char(int8);
	void send_string(int8[]);
	void time_frequency();
	void get_char(int8*);
	void get_string();
	void test(); //该函数为检查收到指令状态的，尚未实现

#endif