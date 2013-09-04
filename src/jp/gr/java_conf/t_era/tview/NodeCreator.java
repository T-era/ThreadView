package jp.gr.java_conf.t_era.tview;

public abstract class NodeCreator<TNode extends Node<TNode>> {
	public final TNode createNode(TNode parent) {
		TNode node = newNode(parent);
		if (parent != null) parent.addChild(node);
		return node;
	}
	abstract protected TNode newNode(TNode parent);
}
