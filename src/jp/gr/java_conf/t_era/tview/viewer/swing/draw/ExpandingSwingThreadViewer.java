package jp.gr.java_conf.t_era.tview.viewer.swing.draw;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import jp.gr.java_conf.t_era.tview.Node;
import jp.gr.java_conf.t_era.tview.Tag;
import jp.gr.java_conf.t_era.tview.viewer.ThreadViewer;
import jp.gr.java_conf.t_era.tview.viewer.swing.draw.MyMouseListener.MyMouseEvent;

public class ExpandingSwingThreadViewer<TNode extends Node<TNode>, TTag extends Tag<TNode>> extends Container implements ThreadViewer<TNode, TTag> {
	private static final long serialVersionUID = 3520151717710564554L;
	private static final Color STRING_COLOR = Color.BLACK;

	private final int heightOfLine;
	private final int widthOfThread;
	private final Color nodeBackColor;
	private final Color tagBackColor;

	private final List<NodeView> allElement = new ArrayList<NodeView>();
	private final List<MyMouseListener<TNode, TTag>> listeners = new ArrayList<MyMouseListener<TNode, TTag>>();

	public ExpandingSwingThreadViewer(int heightOfLine, int widthOfThread, Color nodeBackColor, Color tagBackColor) {
		this.heightOfLine = heightOfLine;
		this.widthOfThread = widthOfThread;
		this.nodeBackColor = nodeBackColor;
		this.tagBackColor = tagBackColor;
		super.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				for (MyMouseListener<TNode, TTag> listener : listeners) {
					listener.mouseClicked(new MyMouseEvent<TNode, TTag>(e, getNodeHere(e.getX(), e.getY()), getTagHere(e.getX(), e.getY())));
				}
			}
		});
}
	public ExpandingSwingThreadViewer(int heightOfLine, int widthOfThread) {
		this(heightOfLine, widthOfThread, Color.WHITE, Color.WHITE);
	}
	private TNode getNodeHere(int x, int y) {
		NodeView nv = getNodeView(x, y);
		return nv == null ? null : nv.node;
	}
	private TTag getTagHere(int x, int y) {
		NodeView nv = getNodeView(x, y);
		return nv == null ? null : nv.tag;
	}
	private NodeView getNodeView(int x, int y) {
		NodeView nearest = null;
		int minDy = heightOfLine / 2;
		for (NodeView temp : allElement) {
			int dy = Math.abs(temp.y - y);
			if (dy < minDy) {
				nearest = temp;
				minDy = dy;
			}
		}
		return nearest;
	}
	public void addMyMouseListener(MyMouseListener<TNode, TTag> listener) {
		listeners.add(listener);
	}

	@Override
	public void paint(Graphics g) {
		super.paintComponents(g);

		for (NodeView view : allElement) {
			view.draw(g);
		}
	}

	@Override
	public void Show(final List<TTag> tags, TNode root, final Map<TNode, Date> timeline) {
		allElement.clear();
		final int width = root.getThreadCount() + 1;
		final int height = timeline.size() + 1;
		Dimension size = new Dimension(width * widthOfThread, height * heightOfLine);
		this.setPreferredSize(size);
		this.setSize(size);

		class NodeAppender {
			List<NodeInTimeline> list = new ArrayList<NodeInTimeline>();

			void showNode(TNode node, int x, int y, NodeView parent) {
				TTag tag = null;
				for (TTag temp : tags) {
					if (temp.getNode().equals(node)) {
						tag = temp;
					}
				}
				NodeView nv = new NodeView(node, tag, x, y, parent);
				allElement.add(nv);

				int dx = 0;
				for (TNode child : node.getChildren()) {
					list.add(new NodeInTimeline(timeline.get(child), child, x + dx * widthOfThread, nv));
					dx += child.getThreadCount();
				}

				if (! list.isEmpty()) {
					Collections.sort(list);
					NodeInTimeline first = list.remove(0);
					showNode(first.node, first.x, y + heightOfLine, first.parent);
				}
			}

			class NodeInTimeline implements Comparable<NodeInTimeline> {
				final Date time;
				final TNode node;
				final int x;
				final NodeView parent;
				NodeInTimeline(Date time, TNode node, int x, NodeView parent) {
					this.time = time;
					this.node = node;
					this.x = x;
					this.parent = parent;
				}
				@Override
				public int compareTo(NodeInTimeline o) {
					return time.compareTo(o.time);
				}
			}
		}

		new NodeAppender().showNode(root, widthOfThread / 2, heightOfLine / 2, null);
	}

	private class NodeView {
		private final int x;
		private final int y;
		private final TNode node;
		private final TTag tag;
		private final NodeView parent;

		public NodeView(TNode node, TTag tag, int x, int y, NodeView parent) {
			this.node = node;
			this.tag = tag;
			this.x = x;
			this.y = y;
			this.parent = parent;
		}
		public void draw(Graphics g) {
			Rectangle2D rect = g.getFontMetrics().getStringBounds(node.toString(), g);
			if (parent != null) {
				g.drawLine(x, y + (int)(rect.getHeight() / 2), parent.x, parent.y + (int)(rect.getHeight() / 2));
			}
			if (tag != null) {
				Rectangle2D tagRect = g.getFontMetrics().getStringBounds(tag.toString(), g);
				centeringDraw(g, tagRect, tag.toString(), x + widthOfThread / 2, y + heightOfLine / 3, tagBackColor);
			}
			centeringDraw(g, rect, node.toString(), x, y, nodeBackColor);
		}
		private void centeringDraw(Graphics g, Rectangle2D rect, String contents, int x, int y, Color backColor) {
			int left = (int)(x - rect.getWidth() / 2);
			int top = (int)(y - rect.getHeight() / 2);

			g.setColor(backColor);
			g.fillRect(left, top, (int)rect.getWidth(), (int)rect.getHeight());
			g.setColor(STRING_COLOR);
			g.drawString(contents, left, top + (int)rect.getHeight());
		}
	}
}
