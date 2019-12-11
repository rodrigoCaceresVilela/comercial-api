package br.com.rcv.comercial.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GerardorDeSenha {

	public static void main(String[] args) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		System.out.println(encoder.encode("123456"));
	}
}