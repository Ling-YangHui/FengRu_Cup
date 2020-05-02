#ifndef __FENGRUCUP_H__
	#define __FENGRUCUP_H__

	#define int8 unsigned char
	#define int16 unsigned int
	#define default_ssid "\"Nanami\""
	#define default_psd "\"Murasame\""
	#define default_IP "49.235.143.220"
	#define default_PORT "7000"
	
	extern int8 wifi_done;
	extern int8 order_state;
	extern int8 get_cache[48];
	extern int8 top;
	void delay_ms(int16);
	void num_init();
	void get_init();
	void uart_init();
	void time_init();
	void send_char(int8);
	void send_string(int8[]);
	void time_frequency();
	void get_char(int8*);
	void get_string();
	void test(); //该函数为检查收到指令状态的，尚未实现
	void open();

#endif