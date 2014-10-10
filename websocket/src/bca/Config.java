package bca;

import javax.enterprise.inject.Produces;

import bca.service.FileImport;

public class Config {
	@FileImport
	@Produces 
	String filePath = "/tmp";
}
