package meetingservice.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;
import meetingservice.entities.File;

public interface FileService{

	Iterable<File> getFiles(Long meetingId);
	
	File Store(Long meetingId, MultipartFile file) throws Exception;

	File findOne(Long id);

	void delete(Long id);

	byte[] ReadFile(File file) throws IOException;
}
