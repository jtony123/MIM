package datastructures;



/**
 * A class that implements the ADT AVL tree by extending BinarySearchTreeR.
 * The remove operation is not supported.
 * 
 * @author Frank M. Carrano
 * @version 2.0
 */
public class AVLTree<T extends Comparable<? super T>> 
             extends BinarySearchTreeR<T>
             implements SearchTreeInterface<T>, java.io.Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AVLTree()
	{
		super();
	} // end default constructor
	
	public AVLTree(T rootEntry)
	{
		super(rootEntry);
	} // end constructor

	// 29.12
	public T add(T newEntry)
	{
	  T result = null;
	  
	  if (isEmpty())
	    setRootNode(new BinaryNode<T>(newEntry));
	  else
	  {
	    BinaryNodeInterface<T> rootNode = getRootNode();
	    result = addEntry(rootNode, newEntry);
	    setRootNode(rebalance(rootNode));
	  } // end if
	  
	  return result;
	} // end add

	// 29.12
	/** Task: Adds newEntry to the non-empty subtree rooted at rootNode. */
	private T addEntry(BinaryNodeInterface<T> rootNode, T newEntry)
	{
	  assert rootNode != null;
	  T result = null;
	  int comparison = newEntry.compareTo(rootNode.getData());
	  
	  if (comparison == 0)
	  {
	    result = rootNode.getData();
	    rootNode.setData(newEntry);
	  }
	  else if (comparison < 0)
	  {
	    if (rootNode.hasLeftChild())
	    {
	      BinaryNodeInterface<T> leftChild = rootNode.getLeftChild();
	      result = addEntry(leftChild, newEntry);
	      rootNode.setLeftChild(rebalance(leftChild));
	    }
	    else
	      rootNode.setLeftChild(new BinaryNode<T>(newEntry));
	  }
	  else
	  {
	    assert comparison > 0;
	    
	    if (rootNode.hasRightChild())
	    {
	      BinaryNodeInterface<T> rightChild = rootNode.getRightChild();
	      result = addEntry(rightChild, newEntry);
	      rootNode.setRightChild(rebalance(rightChild));
	    }
	    else
	      rootNode.setRightChild(new BinaryNode<T>(newEntry));
	  } // end if
	  
	  return result;
	} // end addEntry
	
  public T remove(T entry)
  {
		throw new UnsupportedOperationException();
  } // end remove

	// 29.11
	private BinaryNodeInterface<T> rebalance(BinaryNodeInterface<T> nodeN)
	{
	  int heightDifference = getHeightDifference(nodeN);
	  
	  if (heightDifference > 1)
	  { // left subtree is taller by more than 1, 
	    // so addition was in left subtree
	    if (getHeightDifference(nodeN.getLeftChild()) > 0)
	      // addition was in left subtree of left child
	      nodeN = rotateRight(nodeN);
	    else
	      // addition was in right subtree of left child
	      nodeN = rotateLeftRight(nodeN);
	  }
	  else if (heightDifference < -1)
	  { // right subtree is taller by more than 1, 
	    // so addition was in right subtree
	    if (getHeightDifference(nodeN.getRightChild()) < 0)
	      // addition was in right subtree of right child
	      nodeN = rotateLeft(nodeN);
	    else
	      // addition was in left subtree of right child
	      nodeN = rotateRightLeft(nodeN);
	  } // end if
	  // else nodeN is balanced
	  
	  return nodeN;
	} // end rebalance

	// 29.09
	/** Task: Corrects an imbalance at the node closest to a structural
	 *        change in the left subtree of the node's left child.
	 *  @param nodeN  a node, closest to the newly added leaf, at which 
	 *                an imbalance occurs and that has a left child. */
	private BinaryNodeInterface<T> rotateRight(BinaryNodeInterface<T> nodeN)
	{
	  BinaryNodeInterface<T> nodeC = nodeN.getLeftChild();
	  nodeN.setLeftChild(nodeC.getRightChild());
	  nodeC.setRightChild(nodeN);
	  return nodeC;
	} // end rotateRight
  
  /** Task: Corrects an imbalance at the node closest to a structural
   *        change in the right subtree of the node's right child.
   *  @param nodeN  a node, closest to the newly added leaf, at which an 
   *                imbalance occurs and that has a right child. */
  private BinaryNodeInterface<T> rotateLeft(BinaryNodeInterface<T> nodeN)
  {
    BinaryNodeInterface<T> nodeC = nodeN.getRightChild();
    nodeN.setRightChild(nodeC.getLeftChild());
    nodeC.setLeftChild(nodeN);
    return nodeC;
  } // end rotateLeft
  
	// 29.09
	/** Task: Corrects an imbalance at the node closest to a structural
	 *        change in the left subtree of the node's right child.
	 *  @param nodeN  a node, closest to the newly added leaf, at which 
	 *                an imbalance occurs and that has a right child. */
	private BinaryNodeInterface<T> rotateRightLeft(BinaryNodeInterface<T> nodeN)
	{
	  BinaryNodeInterface<T> nodeC = nodeN.getRightChild();
	  nodeN.setRightChild(rotateRight(nodeC));
	  return rotateLeft(nodeN);
	} // end rotateRightLeft
  
  /** Task: Corrects an imbalance at the node closest to a structural
   *        change in the right subtree of the node's left child.
   *  @param nodeN  a node, closest to the newly added leaf, at which an 
   *                imbalance occurs and that has a left child. */
  private BinaryNodeInterface<T> rotateLeftRight(BinaryNodeInterface<T> nodeN)
  {
    BinaryNodeInterface<T> nodeC = nodeN.getLeftChild();
    nodeN.setLeftChild(rotateLeft(nodeC));
    return rotateRight(nodeN);
  } // end rotateLeftRight

	private int getHeightDifference(BinaryNodeInterface<T> node)
	{
		BinaryNodeInterface<T> left = node.getLeftChild();
		BinaryNodeInterface<T> right = node.getRightChild();

		int leftHeight, rightHeight;

		if (left == null)
			leftHeight = 0;
		else
			leftHeight = left.getHeight();
		
		if (right == null)
			rightHeight = 0;
		else
			rightHeight = right.getHeight();
			
		return leftHeight - rightHeight;
	} // end getHeightDifference
/*	
	// For testing: displays node data in level order
	public void display()
	{
		QueueInterface<BinaryNodeInterface<T>> nodeQueue = new LinkedQueue<BinaryNodeInterface<T>>();
		BinaryNodeInterface<T> root = getRootNode();

		if (root != null)
			nodeQueue.enqueue(root);
		
		int nodesPerLevel = 1;	
		while (!nodeQueue.isEmpty())
			nodesPerLevel = displayNextLevel(nodesPerLevel, nodeQueue);
	} // end display

	private int displayNextLevel(int nodesPerLevel, QueueInterface<BinaryNodeInterface<T>> nodeQueue)
	{
		int nextLevelCount = 0;
		for (int count = 0; count < nodesPerLevel; count++)
		{
			if (!nodeQueue.isEmpty())
			{
				BinaryNodeInterface<T> nextNode = nodeQueue.dequeue();
				BinaryNodeInterface<T> leftChild = nextNode.getLeftChild();
				BinaryNodeInterface<T> rightChild = nextNode.getRightChild();
				
				// add to queue in order of recursive calls
				if (leftChild != null)
				{	nodeQueue.enqueue(leftChild);
					nextLevelCount++;
				} // end if
				
				if (rightChild != null)
				{	nodeQueue.enqueue(rightChild);
					nextLevelCount++;
				} // end if
				
				System.out.print(nextNode.getData() + " ");
			}
			else
				throw new NoSuchElementException();
		} // end for
		System.out.println();
		
		return nextLevelCount;
	} // end displayNextLevel */
} // end AVLTree
