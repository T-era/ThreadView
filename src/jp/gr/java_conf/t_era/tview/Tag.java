package jp.gr.java_conf.t_era.tview;

public abstract class Tag<TNode extends Node<TNode>> {
	private final TNode node;

	public Tag(TNode node) {
		if (node ==null) throw new IllegalArgumentException("Tag can't create with NULL node.");
		this.node = node;
	}

	public TNode getNode() {
		return node;
	}
}
