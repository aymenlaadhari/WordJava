/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dastex.javaword;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author aladhari
 */
public class FileToInputStreamApp {
    public static void main(String[] args) {

	InputStream inputStream = null;
	BufferedReader br = null;

	try {
		// read this file into InputStream
		inputStream = new FileInputStream("C:/Users/aladhari/Documents/NetBeansProjects/WordJava/Test.docx");

		br = new BufferedReader(new InputStreamReader(inputStream));

		StringBuilder sb = new StringBuilder();

		String line;
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}

		System.out.println(sb.toString());
		System.out.println("\nDone!");

	} catch (IOException e) {
	} finally {
		if (inputStream != null) {
			try {
				inputStream.close();
			} catch (IOException e) {
			}
		}
		if (br != null) {
			try {
				br.close();
			} catch (IOException e) {
			}
		}
	}
    }
}
