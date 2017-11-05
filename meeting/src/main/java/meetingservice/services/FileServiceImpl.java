package meetingservice.services;


import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import meetingservice.entities.File;
import meetingservice.repositories.FileRepository;


@Service("fileService")
@Repository
public class FileServiceImpl implements FileService{
 
	 private static String ROOT_DIRECTORY = "files\\meeting";
	
	@Autowired
    private FileRepository repository;

	@Override
	public Iterable<File> getFiles(Long meetingId) {
		Iterable<File> files = repository.findByMeetingId(meetingId);
		return files;
	}

	@Override
	public File Store(Long meetingId, MultipartFile file) throws Exception {
		
		if (file.isEmpty()) 
	        return null;
		
		try {
			String path = String.format("%s\\%s_%s", ROOT_DIRECTORY, UUID.randomUUID().toString(), file.getOriginalFilename());
			saveFile(file, path);
			File entity = new File(file.getOriginalFilename(), path, file.getContentType(), meetingId);
			return repository.save(entity);
		} catch(IOException ex) {
			throw new Exception();
		}catch(Exception ex) {
			throw ex;
		}
	}

	@Override
	public File findOne(Long id) {

		File file = repository.findOne(id);
		return file;
	}

	@Override
	public void delete(Long id) {
		repository.delete(id);
	}
	
	@Override
	public byte[] ReadFile(File file) throws IOException {
		// TODO Auto-generated method stub
		String fileLocation = new java.io.File(file.getPath()).getAbsolutePath();
		return Files.readAllBytes(Paths.get(fileLocation));
	}
	
	private void saveFile(MultipartFile file, String path) throws IOException {
	    byte[] bytes = file.getBytes();
	    String fileLocation = new java.io.File(path).getAbsolutePath();
	    FileOutputStream fos = new FileOutputStream(fileLocation);
	    fos.write(bytes);
	    fos.close();
	    
	}
}
