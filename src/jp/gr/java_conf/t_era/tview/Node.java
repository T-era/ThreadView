package jp.gr.java_conf.t_era.tview;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Node<TNode extends Node<TNode>> {
	/**
	 * 親ノード(システム上唯一の直接上位ノード)
	 */
	private final TNode parentNode;
	/**
	 * (複数ある場合の)上位ノード。マージをしたら複数。
	 */
	private final Set<TNode> upperNode = new HashSet<TNode>();
	private final List<TNode> childNode = new ArrayList<TNode>();

	/**
	 * サブクラスのコンストラクタはこのコンストラクタをコールするとともに、
	 * parentの{@link #addChild(Node)}に生成したインスタンスを渡す必要があります。
	 * @param parent
	 */
	protected Node(TNode parent) {
		this.parentNode = parent;
	}

	/**
	 * NodeCreator の中でのみ呼び出す。
	 * @param newChild
	 */
	final void addChild(TNode newChild) {
		childNode.add(newChild);
	}
	public final void addUpper(TNode newUpper) {
		upperNode.add(newUpper);

	}
	/**
	 * このノードから上位、下位を辿った場合の最大長を求めます。
	 * @return
	 */
	public final int getMaxLength() {
		return getUpperLength() + getLowerLength() + 1;
	}
	private final int getUpperLength() {
		if (parentNode == null) return 0;
		else {
			int max = parentNode.getUpperLength() ;
			for (TNode upper : upperNode) {
				int temp = upper.getUpperLength();
				if (temp > max) {
					max = temp;
				}
			}
			return max + 1;
		}
	}
	private final int getLowerLength() {
		int max = -1;
		for (Node<TNode> child : childNode) {
			int temp = child.getLowerLength();
			if (temp > max) {
				max = temp;
			}
		}
		return max + 1;
	}

	TNode getParent() {
		return parentNode;
	}
	public List<TNode> getChildren() {
		return childNode;
	}
	public int getThreadCount() {
		int sum = childNode.size() == 0 ? 1 : childNode.size();
		for (Node<TNode> child : childNode) {
			sum += child.getThreadCount() - 1;
		}
		return sum;
	}
}
