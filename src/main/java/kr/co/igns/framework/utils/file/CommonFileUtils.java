package kr.co.igns.framework.utils.file;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.sql.Timestamp;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import kr.co.igns.framework.config.exception.CUserDefinedException;
import kr.co.igns.framework.utils.type.DateUtils;

public class CommonFileUtils {
	private static final Logger log = LoggerFactory.getLogger(CommonFileUtils.class);
	private static List<String> fileExtensions = new ArrayList();

	public static boolean isFileExtension(String fileName, List<String> extensions) {
		String[] fileSplit = fileName.split("\\.");
		return extensions.contains(fileSplit[fileSplit.length - 1].toLowerCase());
	}

	public static boolean checkFileExt(HttpServletRequest request, List<String> extensions) {
		boolean flag = true;
		Object files = new HashMap();

		try {
			MultipartHttpServletRequest multipartReq = (MultipartHttpServletRequest) request;
			files = multipartReq.getFileMap();
		} catch (Exception var7) {
			log.debug("No Upload file");
		}

		Iterator var8 = ((Map) files).values().iterator();

		while (var8.hasNext()) {
			MultipartFile file = (MultipartFile) var8.next();
			if (file != null) {
				String realFileNm = file.getOriginalFilename();
				if (!realFileNm.equals("") && !isFileExtension(realFileNm, extensions)) {
					return false;
				}
			}
		}

		return flag;
	}

	public static List<String> getFileExtensions() {
		return fileExtensions;
	}

	public static void setFileExtensions(List<String> fileExtensions) {
		fileExtensions = fileExtensions;
	}

	public static ArrayList<String[]> uploadMutilpleFile(HttpServletRequest request, String fileUploadLocation,
			long fileSize) {
		ArrayList<String[]> fileList = new ArrayList();
		Object files = new HashMap();

		try {
			MultipartHttpServletRequest multipartReq = (MultipartHttpServletRequest) request;
			files = multipartReq.getFileMap();
		} catch (Exception var10) {
			log.debug("No Upload file");
		}

		fileUploadLocation = DateUtils.timeStamp("yyyy/MM/dd");
		Iterator var11 = ((Map) files).values().iterator();

		while (var11.hasNext()) {
			MultipartFile file = (MultipartFile) var11.next();
			if (file != null) {
				String realFileNm = file.getOriginalFilename();
				if (!realFileNm.equals("")) {
					String saveFileNm = changeToSaveFileName(realFileNm);
					fileList.add(uploadToOneFile(request, fileUploadLocation, file, saveFileNm, fileSize));
				}
			}
		}

		return fileList;
	}

	public static String[] uploadToOneFile(HttpServletRequest req, String saveFileLocation, MultipartFile file,
											String saveFileName, long fileSize) {
		String[] fileName = new String[]{saveFileLocation, null, null, String.valueOf(file.getSize())};
		File dir = null;
		if (file.getSize() > fileSize) {
			throw new CUserDefinedException("fileSizeExceed");
		} else {
			String tempSaveFileLocation = saveFileLocation;
			if (!"".equals(file.getOriginalFilename())) {
				fileName[1] = file.getOriginalFilename();
				if (checkFileExtension(saveFileName)) {
					throw new CUserDefinedException("noUploadFile");
				}

				dir = new File(saveFileLocation);
				if (!dir.exists()) {
					dir.mkdirs();
				}

				try {
					fileName[2] = saveFileName;
					FileCopyUtils.copy(file.getInputStream(),
							new FileOutputStream(tempSaveFileLocation + File.separator + saveFileName));
				} catch (Exception var10) {
					throw new CUserDefinedException("fileUploadFail");
				}
			}

			return fileName;
		}
	}

	public static String changeToSaveFileName(String fileName) {
		String[] fileNameSplit = fileName.split("\\.");
		Random rand = new Random(System.currentTimeMillis());
		return "" + System.currentTimeMillis() + Math.round(rand.nextDouble() * 10000.0D) + "."
				+ fileNameSplit[fileNameSplit.length - 1];
	}

	public static boolean checkFileExtension(String fileName) {
		String[] fileSplit = fileName.split("\\.");
		return fileExtensions.contains(fileSplit[fileSplit.length - 1].toLowerCase());
	}

	public static void fileDownload(HttpServletRequest request, HttpServletResponse response, String filePath,
			String fileNm, String realFileNm) {
		String realFilePath = "";
		realFilePath = filePath + File.separator + fileNm;
		log.debug(realFilePath);
		File file = new File(realFilePath);
		if (file.exists()) {
			log.debug("");
			String header = request.getHeader("User-Agent");
			String docName = "";
			OutputStream os = null;
			FileInputStream is = null;

			try {
				if (header.indexOf("MSIE") > -1) {
					docName = URLEncoder.encode(realFileNm, "UTF-8").replaceAll("\\+", "%20");
				} else if (header.indexOf("Trident") > -1) {
					docName = URLEncoder.encode(realFileNm, "UTF-8").replaceAll("\\+", "%20");
				} else if (header.indexOf("Chrome") <= -1) {
					if (header.indexOf("Opera") > -1) {
						docName = new String(realFileNm.getBytes("UTF-8"), "8859_1");
					} else if (header.indexOf("Safari") > -1) {
						docName = new String(realFileNm.getBytes("UTF-8"), "8859_1");
						docName = URLDecoder.decode(docName);
					} else {
						docName = new String(realFileNm.getBytes("UTF-8"), "8859_1");
						docName = URLDecoder.decode(docName);
					}
				} else {
					StringBuffer sb = new StringBuffer();

					for (int i = 0; i < realFileNm.length(); ++i) {
						char c = realFileNm.charAt(i);
						if (c > 126) {
							sb.append(URLEncoder.encode(Character.toString(c), "UTF-8"));
						} else {
							sb.append((char) c);
						}
					}

					docName = sb.toString();
				}

				response.setContentType("application/octet-stream; charset=utf-8");
				response.setContentLength((int) file.length());
				response.setHeader("Content-Disposition", docName);
				response.setHeader("Content-Transfer-Encoding", "binary");
				response.setCharacterEncoding("UTF-8");
				os = response.getOutputStream();
				is = null;
				is = new FileInputStream(file);
				FileCopyUtils.copy(is, os);
				os.flush();
			} catch (FileNotFoundException var25) {
				var25.printStackTrace();
				throw new CUserDefinedException("fileNotFound");
			} catch (IOException var26) {
				var26.printStackTrace();
				throw new CUserDefinedException("fileDownloadFail");
			} finally {
				if (os != null) {
					try {
						os.close();
					} catch (IOException var24) {
						var24.printStackTrace();
					}
				}

				if (is != null) {
					try {
						is.close();
					} catch (IOException var23) {
						var23.printStackTrace();
					}
				}

			}

		} else {
			log.debug("");
			throw new CUserDefinedException("fileNotFound");
		}
	}

	public static void deleteFile(HttpServletRequest request, String fullPath, String file) {
		String realPath = "";
		File isDeleteFile = new File(file);
		if (isDeleteFile.exists()) {
			isDeleteFile.delete();
		}

	}

	public static void copy(String fOrg, String fTarget, String fTargetName) {
		try {
			File fsTarget = new File(fTarget);
			if (!fsTarget.exists()) {
				fsTarget.mkdirs();
			}

			String fsTargetName = fsTarget + File.separator + fTargetName;
			FileInputStream inputStream = new FileInputStream(fOrg);
			FileOutputStream outputStream = new FileOutputStream(fsTargetName, true);
			FileChannel fcin = inputStream.getChannel();
			FileChannel fcout = outputStream.getChannel();
			long size = fcin.size();
			fcin.transferTo(0L, size, fcout);
			fcout.close();
			fcin.close();
			inputStream.close();
			outputStream.close();
		} catch (IOException var11) {
			var11.printStackTrace();
			throw new CUserDefinedException("fileNotCopy");
		}
	}

	public static String createFileName(String fileName) {
		String[] fileSplit = fileName.split("\\.");
		String extension = fileSplit[fileSplit.length - 1].toLowerCase();
		UUID.randomUUID().toString();
		return extension;
	}

	public static String createFilePath(String prefix) {
		Calendar cal = Calendar.getInstance();
		String year = Integer.toString(cal.get(1));
		String month = Integer.toString(cal.get(2) + 1);
		String date = Integer.toString(cal.get(5));
		String var10001 = File.separator;
		String var10003 = File.separator;
		String var10005 = File.separator;
		return (String) date;
	}

	static {
		fileExtensions.addAll(Arrays.asList("asp", "jsp", "html", "htm", "com", "exe", "sh", "php"));
	}
}