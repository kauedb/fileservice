package de.exb.platform.cloud.fileservice.fileservice.domain;

import javax.inject.Named;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

@Named
public class FileServiceImpl implements FileService {

	@Override
	public OutputStream openForWriting(final String aSessionId, final URL aPath, final boolean aAppend)
			throws FileServiceException {
		try {
			return new FileOutputStream(new File(decodePath(aPath)), aAppend);
		} catch (final FileNotFoundException e) {
			throw new FileServiceException("cannot open file", e);
		}
	}

	@Override
	public InputStream openForReading(final String aSessionId, final URL aPath) throws FileServiceException {
		try {
			return new FileInputStream(new File(decodePath(aPath)));
		} catch (final FileNotFoundException e) {
			throw new FileServiceException("cannot open file", e);
		}
	}

	@Override
	public URL construct(final String aSessionId, final String aPath) throws FileServiceException {
		try {
			return new File(aPath).toURI().toURL();
		} catch (final MalformedURLException e) {
			throw new FileServiceException("cannot construct file url", e);
		}
	}

	@Override
	public URL construct(final String aSessionId, final URL aParentPath, final String aChildPath)
			throws FileServiceException {
		try {
			return new File(new File(decodePath(aParentPath)), aChildPath).toURI().toURL();
		} catch (final MalformedURLException e) {
			throw new FileServiceException("cannot construct file url", e);
		}
	}

	@Override
	public void createNewFile(final String aSessionId, final URL aPath) throws FileServiceException {
		try {
			if (!new File(decodePath(aPath)).createNewFile()) {
				throw new FileServiceException("file already exists: " + aPath);
			}
		} catch (final IOException e) {
			throw new FileServiceException("cannot create new file", e);
		}
	}

	@Override
	public void mkdir(final String aSessionId, final URL aPath) throws FileServiceException {
		if (!new File(decodePath(aPath)).mkdir()) {
			throw new FileServiceException("failed to create directory");
		}
	}

	@Override
	public void mkdirs(final String aSessionId, final URL aPath) throws FileServiceException {
		if (!new File(decodePath(aPath)).mkdirs()) {
			throw new FileServiceException("failed to create directories");
		}
	}

	@Override
	public List<URL> list(final String aSessionId, final URL aPath) throws FileServiceException {
		try {
			final List<URL> files = new ArrayList<URL>();
			for (final File f : new File(decodePath(aPath)).listFiles()) {
				files.add(f.toURI().toURL());
			}
			return files;
		} catch (final MalformedURLException e) {
			throw new FileServiceException("cannot list files", e);
		}
	}

	@Override
	public void delete(final String aSessionId, final URL aPath, final boolean aRecursive)
			throws FileServiceException {

		final File f = new File(decodePath(aPath));
		if (aRecursive) {
			final Path path = Paths.get(f.getAbsolutePath());
			try {
				Files.walkFileTree(path, new SimpleFileVisitor<Path>() {

					@Override
					public FileVisitResult visitFile(final Path aFile, final BasicFileAttributes aAttrs)
							throws IOException {
						Files.delete(aFile);
						return FileVisitResult.CONTINUE;
					}

					@Override
					public FileVisitResult postVisitDirectory(final Path aDir, final IOException aExc)
							throws IOException {
						Files.delete(aDir);
						return FileVisitResult.CONTINUE;
					}
				});
			} catch (final IOException e) {
				throw new FileServiceException("cannot delete files", e);
			}
		} else {
			if (!f.delete()) {
				throw new FileServiceException("cannot delete file/directory");
			}
		}
	}

	@Override
	public boolean exists(final String aSessionId, final URL aPath) throws FileServiceException {
		return new File(decodePath(aPath)).exists();
	}

	@Override
	public boolean isFile(final String aSessionId, final URL aPath) throws FileServiceException {
		return new File(decodePath(aPath)).isFile();
	}

	@Override
	public boolean isDirectory(final String aSessionId, final URL aPath) throws FileServiceException {
		return new File(decodePath(aPath)).isDirectory();
	}

	@Override
	public long getSize(final String aSessionId, final URL aPath) throws FileServiceException {
		final File f = new File(decodePath(aPath));
		if (!f.exists()) {
			throw new IllegalArgumentException(f + " does not exist");
		}
		if (f.isDirectory()) {
			return sizeOfDirectory(f);
		}
		return f.length();
	}

	@Override
	public URL getParent(final String aSessionId, final URL aPath) throws FileServiceException {
		try {
			return new File(aPath.getPath()).getParentFile().toURI().toURL();
		} catch (final MalformedURLException e) {
			throw new FileServiceException("cannot construct file url", e);
		}
	}

	@Override
	public String getName(final String aSessionId, final URL aPath) throws FileServiceException {
		return new File(decodePath(aPath)).getName();
	}

	private long sizeOfDirectory(final File aDirectory) {
		final File[] files = aDirectory.listFiles();
		if (files == null) {
			return 0;
		}
		long size = 0;
		for (final File file : files) {
			if (file.isDirectory()) {
				size += sizeOfDirectory(file);
			} else {
				size += file.length();
			}
			if (size < 0) {
				break;
			}
		}
		return size;
	}

	private String decodePath(final URL aPath) throws FileServiceException {
		try {
			return URLDecoder.decode(aPath.getPath(), "UTF-8");
		} catch (final UnsupportedEncodingException e) {
			throw new FileServiceException("cannot decode path", e);
		}
	}
}
