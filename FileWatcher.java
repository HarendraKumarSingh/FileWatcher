package com.hrks.FileWatch;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

/**
 * NOTE : This Class uses java 7 API java.nio.file.* , so it requires JDK 7 or
 * above.
 *  
 * @author <a href="harendmgr@gmail.com">harendrasingh</a>
 *
 */

public class FileWatcher implements Runnable {

	private WatchService watchService;
	private WatchKey watchKey;
	private Thread dirMonitorThread;

	/**
	 * Register the given file system directory with the WatchService
	 */
	void register(String directoryPath) throws IOException {

		System.out.println("templateDirPath : " + directoryPath);
		Path faxFolder = Paths.get(directoryPath);
		try {
			watchService = FileSystems.getDefault().newWatchService();
		} catch (IOException e1) {
			// handle the exception as per your convenience
			System.out.println("directoryPath NOT found");
			e1.printStackTrace();
		}
		try {
			faxFolder.register(watchService,
					StandardWatchEventKinds.ENTRY_CREATE,
					StandardWatchEventKinds.ENTRY_DELETE);
		} catch (IOException e) {
			// handle the exception as per your convenience
			e.printStackTrace();
		}

		try {
			watchKey = watchService.take();
		} catch (InterruptedException e) {
			// handle the exception as per your convenience
			e.printStackTrace();
		}
		startWatcher(directoryPath);
	}

	/**
	 * 
	 * @param directoryPath start the watcher process in background to monitor the files in this directoryPath.
	 */
	public void startWatcher(String directoryPath) {

		boolean valid = true;

		do {
			for (WatchEvent event : watchKey.pollEvents()) {
				event.kind();
				String eventType = "";
				String tempPath = directoryPath;
				String fileName = "";

				// Other event lists are : ENTRY_CREATE ,ENTRY_DELETE ,
				// ENTRY_MODIFY , OVERFLOW
				if (StandardWatchEventKinds.ENTRY_CREATE.equals(event.kind())) {

					fileName = event.context().toString();

					System.out.println("File Created:" + fileName
							+ ", EventKind : " + event.kind());

					eventType = event.kind().toString();

				} else if (StandardWatchEventKinds.ENTRY_DELETE.equals(event
						.kind())) {

					fileName = event.context().toString();

					System.out.println("File Deleted:" + fileName
							+ ", EventKind : " + event.kind());

					eventType = event.kind().toString();

				}
				// using this(directoryPath, modified/created filename and the
				// eventType on it) you can even update your any DOM
				// object/chache for any further processing.
				// XYZ.refreshFileObjectCache( directoryPath + fileName,
				// eventType);
			}
			valid = watchKey.reset();

		} while (valid);

	}

	/**
	 * Interrupt/stop the fileWatcher thread process
	 */
	public void stop() {
		dirMonitorThread.interrupt();
		try {
			this.watchService.close();
		} catch (IOException e) {
			// handle the exception as per your convenience
			e.printStackTrace();
		}
	}

	/**
	 * Override the Runnable run method to initiate the watcher process. Here
	 * provide the fileWatcher source i.e the directory path to be monitored
	 */
	public void run() {
		FileWatcher fileWatcher = null;
		fileWatcher = new FileWatcher();

		 String directoryPath = "<FileWatcher-directory-path>";

		try {
			fileWatcher.register(directoryPath);
		} catch (IOException e) {
			// handle the exception as per your convenience
			e.printStackTrace();
		}
	}

	/**
	 * optional main body
	 */
	public static void main(String[] args) {
		FileWatcher fileWatcher = new FileWatcher();

		fileWatcher.dirMonitorThread = new Thread(new FileWatcher());
		fileWatcher.dirMonitorThread.start();
	}
}
