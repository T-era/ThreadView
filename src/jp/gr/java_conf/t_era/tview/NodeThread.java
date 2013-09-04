package jp.gr.java_conf.t_era.tview;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.gr.java_conf.t_era.tview.viewer.ThreadViewer;

public class NodeThread<TNode extends Node<TNode>, TTag extends Tag<TNode>> {
	private TNode currentNode;

	/**
	 * current 以外の末端ノード
	 */
	private final List<TNode> leafNode = new ArrayList<TNode>();
	private final List<TTag> tags = new ArrayList<TTag>();
	private final Map<TNode, Date> timeline = new HashMap<TNode, Date>();

	public NodeThread() {
		currentNode = null;
	}

	public TNode addNode(NodeCreator<TNode> creator) {
		currentNode = creator.createNode(currentNode);
		timeline.put(currentNode, new Date());
		return currentNode;
	}

	/**
	 * 指定されたノードから、枝分かれ派生します。
	 * @param creator
	 * @param tag
	 */
	public TNode addNodeToTag(NodeCreator<TNode> creator, TTag tag) {
		if (currentNode == null) throw new IllegalStateException("Tag in EMPTY thread??");
		if (! tags.contains(tag)) throw new IllegalArgumentException("Where tag from??");

		return addNodeToMyNode(creator, tag.getNode());
	}
	/**
	 * 指定されたノードから、枝分かれ派生します。
	 * @param creator
	 * @param tag
	 */
	public TNode addNodeToTag(NodeCreator<TNode> creator, int tagIndex) {
		if (currentNode == null) throw new IllegalStateException("Tag in EMPTY thread??");
		if (tags.size() > tagIndex) throw new IllegalArgumentException();

		TTag tag = tags.get(tagIndex);
		return addNodeToMyNode(creator, tag.getNode());
	}
	/**
	 * 指定されたノードから、枝分かれ派生します。
	 * でも指定されたノードがcurrentNodeだったら、{@link NodeThread#addNode}と同じ。
	 * @param creator
	 * @param parent
	 */
	public TNode addNodeToNode(NodeCreator<TNode> creator, TNode parent) {
		if (currentNode == null) throw new IllegalStateException("Tag in EMPTY thread??");
		if (! timeline.containsKey(parent)) throw new IllegalArgumentException("Where parent from??");

		return addNodeToMyNode(creator, parent);
	}
	private TNode addNodeToMyNode(NodeCreator<TNode> creator, TNode myNode) {
		if (myNode.equals(currentNode)) {
			return addNode(creator);
		} else {
			leafNode.add(currentNode);
			currentNode = creator.createNode(myNode);
			timeline.put(currentNode, new Date());
		}
		return currentNode;
	}

	/**
	 * Leafの内の一つをCurrentに指定します。
	 * @param node
	 */
	public void setCurrent(TNode node) {
		if (! leafNode.contains(node)) throw new IllegalStateException();
		leafNode.remove(node);
		if (currentNode != null) {
			leafNode.add(currentNode);
		}
		currentNode = node;
	}
	/**
	 * Leafの内の一つをCurrentに指定します。
	 * @param node
	 */
	public void setCurrent(int leafIndex) {
		if (leafNode.size() <= leafIndex) throw new IllegalStateException();

		TNode node = leafNode.remove(leafIndex);
		if (currentNode != null) {
			leafNode.add(currentNode);
		}
		currentNode = node;
	}

	public void setCurrentInHistory(TNode node) {
		if (! timeline.containsKey(node)) throw new IllegalArgumentException();

		if (leafNode.contains(node)) leafNode.remove(node);
		if (currentNode != null && currentNode != node) {
			leafNode.add(currentNode);
		}
		currentNode = node;
	}

	/**
	 * Currentノードに、タグをつけます。
	 * @param creator
	 */
	public TTag addTag(TagCreator<TNode, TTag> creator) {
		TTag newTag = creator.newTag(currentNode);
		tags.add(newTag);
		return newTag;
	}

	/**
	 * 指定された方法で、このスレッドを表示します。
	 * @param viewer
	 */
	public void show(ThreadViewer<TNode, TTag> viewer) {
		viewer.Show(tags, getRout(), timeline);
	}

	public TNode getRout() {
		TNode root = currentNode;
		while (root.getParent() != null) {
			root = root.getParent();
		}
		return root;
	}
}
