package jp.gr.java_conf.t_era.tview.viewer.swing.draw;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;

public class FloatSizeShowString {
	private final String contents;
	private final String forCPadding;

	private final String additional;
	private final String forAPadding;

	private final double x;
	private final double y;

	public FloatSizeShowString(String contents, String additional, double x, double y) {
		this.contents = contents;
		this.forCPadding = contents + "XX";
		this.additional = additional;
		this.forAPadding = additional + "XX";
		this.x = x;
		this.y = y;
	}

//	public void show(Graphics g, Dimension containerSize) {
//		Font f = g.getFont();
//
//		Rectangle2D padSize = f.getStringBounds(forPadding, g.getFontMetrics().getFontRenderContext());
//		Point bkgTL = getTopLeft(containerSize, padSize);
//		g.clearRect(bkgTL.x, bkgTL.y - (int)padSize.getHeight(), (int)padSize.getWidth(), (int)padSize.getHeight());
//
//		Rectangle2D contentsSize = f.getStringBounds(contents, g.getFontMetrics().getFontRenderContext());
//		Point strTL = getTopLeft(containerSize, contentsSize);
//		drawString(g, contents, contentsSize, strTL);
//	}
	public void show(Graphics g, Dimension containerSize) {
		Font f = g.getFont();

		Rectangle2D padSize = f.getStringBounds(forCPadding, g.getFontMetrics().getFontRenderContext());
		Point bkgTL = getTopLeft(containerSize, padSize);
		g.clearRect(bkgTL.x, bkgTL.y - (int)padSize.getHeight(), (int)padSize.getWidth(), (int)padSize.getHeight());

		Rectangle2D contentsSize = f.getStringBounds(contents, g.getFontMetrics().getFontRenderContext());
		Point strTL = getTopLeft(containerSize, contentsSize);
		drawString(g, contents, contentsSize, strTL);
		if (additional != null) {
			Rectangle2D padSize2 = f.getStringBounds(forAPadding, g.getFontMetrics().getFontRenderContext());
			Point bkgTL2 = getTopLeft(containerSize, padSize, (int)padSize.getWidth(), 0);
			g.clearRect(bkgTL2.x, bkgTL2.y - (int)padSize2.getHeight(), (int)padSize2.getWidth(), (int)padSize2.getHeight());

			Rectangle2D additionSize = f.getStringBounds(additional, g.getFontMetrics().getFontRenderContext());
			Point strTL2 = getTopLeft(containerSize, contentsSize, (int)padSize.getWidth(), 0);
			drawString(g, additional, additionSize, strTL2);
			g.drawRect(bkgTL2.x, bkgTL2.y - (int)padSize2.getHeight(), (int)padSize2.getWidth(), (int)padSize2.getHeight());
		}
	}
	private Point getTopLeft(Dimension containerSize, Rectangle2D contentsSize) {
		return getTopLeft(containerSize, contentsSize, 0, 0);
	}
	private Point getTopLeft(Dimension containerSize, Rectangle2D contentsSize, int dx, int dy) {
		int drawX = (int)(containerSize.width * x - contentsSize.getWidth() / 2) + dx;
		int drawY = (int)(containerSize.height * y) + dy;
		return new Point(drawX, drawY);
	}

	private static void drawString(Graphics g, String contents, Rectangle2D contentsSize, Point topLeft) {
		int adjustedY = topLeft.y - (int)(contentsSize.getHeight() / 4.0);
		g.drawString(contents, topLeft.x, adjustedY);
	}
}
