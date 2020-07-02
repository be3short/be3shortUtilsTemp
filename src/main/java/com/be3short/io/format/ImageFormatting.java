package com.be3short.io.format;

public enum ImageFormatting implements FileFormatProperties<ImageFormatting>
{

	PNG(
		"PNG",
		".png",
		true,
		true),
	JPEG(
		"JPEG",
		".jpg",
		true,
		true),
	GIF(
		"gif",
		".gif",
		true,
		true),
	BMP(
		"BMP",
		".bmp",
		true,
		true),
	SVG(
		"SVG",
		".svg",
		false),
	EPS(
		"EPS",
		".eps",
		false);

	private final FileFormatUtilities<ImageFormatting> toolbox;
	public final String extension;
	public final String name;
	public final boolean image;
	public final boolean needsBackground;

	private ImageFormatting(String name, String extension, boolean image)
	{
		this.name = name;
		this.image = image;
		this.needsBackground = false;
		this.extension = extension;
		toolbox = new FileFormatUtilities<ImageFormatting>(this);
	}

	private ImageFormatting(String name, String extension, boolean image, boolean background)
	{
		this.name = name;
		this.image = image;
		this.needsBackground = background;
		this.extension = extension;
		toolbox = new FileFormatUtilities<ImageFormatting>(this);

	}

	@Override
	public String getFileExtension()
	{
		// TODO Auto-generated method stub
		return this.extension;
	}

	@Override
	public String getFormatName()
	{
		// TODO Auto-generated method stub
		return this.name();
	}

	@Override
	public FileFormatUtilities<ImageFormatting> getUtilities()
	{
		// TODO Auto-generated method stub
		return toolbox;
	}

}