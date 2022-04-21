package com.shopme.admin;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtil {
//ok
	public static void saveFile(String uploadDir,String fileName,
			MultipartFile multipartFile) throws IOException {
		//first we need to get path and to create the directory with uploadDir
		Path uploadPath=Paths.get(uploadDir);
		//and if doesnt existe we create this directory 
		if(!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}
		//then we save the data in multipartFile object here as a file in the file system
		try (InputStream inputStream=multipartFile.getInputStream()){
			//we create the path for the file path..uploadPath
			Path filePath=uploadPath.resolve(fileName);
			Files.copy(inputStream, filePath,StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException ex) {
			throw new IOException("Could not save file: "+fileName,ex);
		}
		
	}

}
