package de.exb.platform.cloud.fileservice.fileservice.domain;

import javax.validation.constraints.NotNull;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;

public interface FileService {

	@NotNull
	OutputStream openForWriting(@NotNull final String aSessionId, @NotNull final URL aPath,
								final boolean aAppend)
		throws FileServiceException;

	@NotNull
	InputStream openForReading(@NotNull final String aSessionId, @NotNull final URL aPath)
			throws FileServiceException;

	@NotNull
	URL construct(@NotNull final String aSessionId, @NotNull final String aPath) throws FileServiceException;

	@NotNull
	URL construct(@NotNull final String aSessionId, @NotNull final URL aParentPath, @NotNull final String aChildPath)
		throws FileServiceException;

	void createNewFile(@NotNull final String aSessionId, @NotNull final URL aPath) throws FileServiceException;

	void mkdir(@NotNull final String aSessionId, @NotNull final URL aPath) throws FileServiceException;

	void mkdirs(@NotNull final String aSessionId, @NotNull final URL aPath) throws FileServiceException;

	@NotNull
	List<URL> list(@NotNull final String aSessionId, @NotNull final URL aPath) throws FileServiceException;

	void delete(@NotNull final String aSessionId, @NotNull final URL aPath, final boolean aRecursive)
			throws FileServiceException;

	boolean exists(@NotNull final String aSessionId, @NotNull final URL aPath) throws FileServiceException;

	boolean isFile(@NotNull final String aSessionId, @NotNull final URL aPath) throws FileServiceException;

	boolean isDirectory(@NotNull final String aSessionId, @NotNull final URL aPath) throws FileServiceException;

	long getSize(@NotNull final String aSessionId, @NotNull final URL aPath) throws FileServiceException;

	@NotNull
	URL getParent(@NotNull final String aSessionId, @NotNull final URL aPath) throws FileServiceException;

	@NotNull
	String getName(@NotNull final String aSessionId, @NotNull final URL aPath) throws FileServiceException;
}
