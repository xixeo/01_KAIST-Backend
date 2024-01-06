package kr.co.igns.framework.utils.file;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.io.ZipInputStream;
import net.lingala.zip4j.io.ZipOutputStream;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.unzip.UnzipUtil;
import net.lingala.zip4j.util.Zip4jConstants;

/**
 * 암호 압축
 * @author choi
 *
 */
public class Zip4jUtil {

	private static final String PASSWORD = "16611878";
	 private static final int BUFFER_SIZE = 1024*2;
	/**
	 * 암호 압축해제
	 * @param zipPath
	 * @param targetDir
	 */
	public static void unzipWithPassword(String zipPath, String targetDir) {
		try {

			ZipFile zipFile = new ZipFile(zipPath);
			
			if (zipFile.isEncrypted()) {
				zipFile.setPassword(PASSWORD);
			}
			
			@SuppressWarnings("unchecked")
			List<FileHeader> fileHeaderList = zipFile.getFileHeaders();
			
			for (int i = 0; i < fileHeaderList.size(); i++) {
				FileHeader fileHeader = (FileHeader)fileHeaderList.get(i);
				if (fileHeader != null) {
					
					String outFilePath = targetDir + "/" + fileHeader.getFileName();
					
					File outFile = new File(outFilePath);
					
					if (fileHeader.isDirectory()) {
						outFile.mkdirs();
						continue;
					}
					
					File parentDir = outFile.getParentFile();
					if (!parentDir.exists()) {
						parentDir.mkdirs();
					}
					
					ZipInputStream input = zipFile.getInputStream(fileHeader);
					
					OutputStream output = new FileOutputStream(outFile);
					
					int readLen = -1;
					byte[] buff = new byte[4096];
					
					while ((readLen = input.read(buff)) != -1) {
						output.write(buff, 0, readLen);
					}
					
					output.close();
					input.close();
					
					UnzipUtil.applyFileAttributes(fileHeader, outFile);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 암호 압축
	 * @param inputPath
	 * @param outputPath
	 */
	public static int zipWithPassword(String inputPath, String outputPath, boolean passwordFlag) {
		int fileCount = 0;
		try {
			
			File sourceFile = new File(inputPath);

			ArrayList<File> filesToAdd = new ArrayList<File>();

			for (File file : sourceFile.listFiles()) {
				filesToAdd.add(file);
			}
			
			System.out.println(filesToAdd.size());
			
			//압축할 파일이 없으면 종료
			if (filesToAdd.size() == 0) {
				return fileCount;
			}
			
			ZipOutputStream output = new ZipOutputStream(new FileOutputStream(new File(outputPath)));

			ZipParameters parameters = new ZipParameters();
			parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
			parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
			parameters.setEncryptFiles(passwordFlag);
			parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);
			parameters.setPassword(PASSWORD);

			for (int i = 0; i < filesToAdd.size(); i++) {
				File file = (File)filesToAdd.get(i);

				output.putNextEntry(file,parameters);

				if (file.isDirectory()) {
					output.closeEntry();
					continue;
				}

				InputStream input = new FileInputStream(file);
				byte[] readBuff = new byte[4096];
				int readLen = -1;

				while ((readLen = input.read(readBuff)) != -1) {
					output.write(readBuff, 0, readLen);
				}

				output.closeEntry();
				input.close();
				
				fileCount++;
			}

			output.finish();
			output.close();
			
			ZipFile zipFile = new ZipFile(outputPath);
			
			//ZIP파일을 분석할수 없으면 삭제
			if (!zipFile.isValidZipFile()) {
				File file = new File(outputPath);
				file.delete();
				return 0;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileCount;
	}
	
	/**
     * 하위폴더까지 압축
     * @param sourceFile
     * @param sourcePath
     * @param zos
     * @throws Exception
     */
	
    public static void zipEntry(File sourceFile, String sourcePath, java.util.zip.ZipOutputStream zos) throws Exception {
        // sourceFile 이 디렉토리인 경우 하위 파일 리스트 가져와 재귀호출
        if (sourceFile.isDirectory()) {
            if (sourceFile.getName().equalsIgnoreCase(".metadata")) { // .metadata 디렉토리 return
                return;
            }
            File[] fileArray = sourceFile.listFiles(); // sourceFile 의 하위 파일 리스트
            for (int i = 0; i < fileArray.length; i++) {
                zipEntry(fileArray[i], sourcePath, zos); // 재귀 호출
            }
        } else { // sourcehFile 이 디렉토리가 아닌 경우
            BufferedInputStream bis = null;
            try {
                String sFilePath = sourceFile.getPath();
                String zipEntryName = sFilePath.substring(sourcePath.length()-3, sFilePath.length());
                System.out.println("sFilePath: "+sFilePath);
                System.out.println("sourcePath: "+sourcePath);
                System.out.println(zipEntryName);
                bis = new BufferedInputStream(new FileInputStream(sourceFile));
                ZipEntry zentry = new ZipEntry(zipEntryName);
                zentry.setTime(sourceFile.lastModified());
                zos.putNextEntry(zentry);

                byte[] buffer = new byte[BUFFER_SIZE];
                int cnt = 0;
                while ((cnt = bis.read(buffer, 0, BUFFER_SIZE)) != -1) {
                    zos.write(buffer, 0, cnt);
                }
                zos.closeEntry();
            } finally {
                if (bis != null) {
                    bis.close();
                }
            }
        }
    }
    
    public static void dirCopy(File copyDir, File targetDir) {
		
		if (copyDir.isDirectory()) {
			targetDir.mkdir();
			String[] children = copyDir.list();
			
			for (int i=0; i<children.length; i++) {
				dirCopy(new File(copyDir, children[i]), 
						new File(targetDir, children[i]));
			}
        }else {
            try {
                FileInputStream fis = new FileInputStream(copyDir);
                BufferedInputStream bis = new BufferedInputStream(fis, 1024);
                
                FileOutputStream fos = new FileOutputStream(targetDir);
                BufferedOutputStream bos = new BufferedOutputStream(fos, 1024);
                
                int len= 0; byte[] buf = new byte[1024];
                
                while((len = bis.read(buf)) != -1) {
                     bos.write(buf, 0, len);
                     bos.flush();
                }
                fis.close(); fos.close();
                bis.close(); bos.close();
                
            } catch(FileNotFoundException fn) {
                fn.printStackTrace();
                
            } catch(IOException ie) {
                ie.printStackTrace();
            }
        }
	}
    
 // 파일을 이동하는 메소드
 	public static void fileMove(String inFileName, String outFileName) {
 		try {
 			FileInputStream fis = new FileInputStream(inFileName);
 			FileOutputStream fos = new FileOutputStream(outFileName);

 			byte[] buffer = new byte[1024];

 			int length;

 			while ((length = fis.read(buffer)) > 0) {
 				fos.write(buffer, 0, length);
 			}
 		
 			fis.close();
 			fos.close();
 			
 			deleteFile(inFileName);

 		} catch (IOException e) {
 			e.printStackTrace();
 		}
 	}
 	
 	 public static void deleteFile(String deleteFileName) {
 		  File deleteFile = new File(deleteFileName);
 		  deleteFile.delete();
 	}
 	
 	 public static boolean deleteDir(File path) {
 		 if(!path.exists()) {
 			 return false;
 		 }

 		 File[] files = path.listFiles();
 		 for (File file : files) {
 			 if (file.isDirectory()) {
 				deleteDir(file);
 			 } else {
 				 file.delete();
 			 }
 		 }

 		 return path.delete();
 	 }
}