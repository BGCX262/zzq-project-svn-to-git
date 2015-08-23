package cn.thirdgwin.utils;

import cn.thirdgwin.lib.DevConfig;
import cn.thirdgwin.lib.GLLib;
import cn.thirdgwin.lib.Utils;
//--------------------------------------------------------------------------------

//You can
//!if defined(DOJA) || MIDP_VERSION==10


//--------------------------------------------------------------------------------


/// A* (A star) Pathfinding class. A* incrementally searches all routes leading from the starting point until it finds the shortest path to a goal. Like all informed search algorithms, it searches first the routes that appear to be most likely to lead towards the goal. What sets A* apart from a greedy best-first search is that it also takes the distance already traveled into account (the g(x) part of the heuristic is the cost from the start, and not simply the local cost from the previously expanded node).
/// <p> If you want to have more information about the algorythm used in this class, see http://www.policyalmanac.org/games/aStarTutorial.htm or http://en.wikipedia.org/wiki/A%2A

public class PathFinding
{
	public static final int	kDirUp				= 0;
	public static final int	kDirDown			= 1;
	public static final int	kDirLeft			= 2;
	public static final int	kDirRight			= 3;
	public static final int	kDirUpLeft			= 4;
	public static final int	kDirUpRight			= 5;
	public static final int	kDirDownLeft		= 6;
	public static final int	kDirDownRight		= 7;

	private static int kDirPrecalc[] 			= {	0, -1,	// Up
													0, 	1,	// Down
												   -1,  0,	// Left
													1, 	0,  // Right
												   -1, -1,  // Up Left
													1, -1,  // Up Right
												   -1,  1,  // Down Left
													1, 	1}; // Down Right


	private short[] 	m_nodeParent;
	private short[] 	m_nodePrev;
	private short[] 	m_nodeNext;
	private short[] 	m_nodeG;
	private short[] 	m_nodeH;

	private int			m_nCostMove;
	private int			m_nCostMoveDiag;
	private int			m_nCostChangeDir;
	private int			m_nUseDirectionCount;

	private int			m_nMapW;
	private int			m_nMapH;
	private byte[]		m_pPhysMap;
	private int			m_nPhysMapMask;

	private short[]		m_path;
	private int			m_pathIdx;

	private int			m_openedSortedList;

	private boolean		m_init;


	//--------------------------------------------------------------------------------------------------------------------
	/// Empty constructor.
	/// &note Call PathFinding_Init to initialize the class.
	//--------------------------------------------------------------------------------------------------------------------
	public PathFinding()
	{
		m_init = false;
	}


	//--------------------------------------------------------------------------
	/// PathFinding_Init must be used once to initialize the PathFinding engine, or if you want to change the parameters of the search.
	/// &param nMapWidth The witdh of your collision map grid.
	/// &param nMapHeight The height of your collision map grid.
	/// &param pPhysicalMap The coolision map, single dimension byte array. The format is the same as a pixel array, a cell is found by cell=((y*width)+x). For the moment, the internal format of this array is 0=freecell anything else is blocked.
	/// &param nCostMove is the cost of moving verticaly or horizontaly (but not both). Usually you should use 10.
	/// &param nCostMoveDiag is the cost of moving verticaly and horizontaly at the same time. Usually you should use 14. Its not used if nDirCount is 4.
	/// &param nCostChangeDir is the cost of changing direction. Use 0 if there is no cost involve in changing direction. Usually you should use 10.
	/// &param nDirCount is used to specify if the algo should look at diagonals possibilities (8) or only the minimal ones (4).
	//--------------------------------------------------------------------------
	public void PathFinding_Init (int nMapWidth, int nMapHeight, byte[] pPhysicalMap, int nCostMove, int nCostMoveDiag, int nCostChangeDir, int nDirCount)
	{
	    PathFinding_Init(nMapWidth, nMapHeight, pPhysicalMap, nCostMove, nCostMoveDiag, nCostChangeDir, nDirCount, 0xFFFFFFFF);
    }

	//--------------------------------------------------------------------------
	/// PathFinding_Init must be used once to initialize the PathFinding engine, or if you want to change the parameters of the search.
	/// &param nMapWidth The witdh of your collision map grid.
	/// &param nMapHeight The height of your collision map grid.
	/// &param pPhysicalMap The coolision map, single dimension byte array. The format is the same as a pixel array, a cell is found by cell=((y*width)+x). For the moment, the internal format of this array is 0=freecell anything else is blocked.
	/// &param nCostMove is the cost of moving verticaly or horizontaly (but not both). Usually you should use 10.
	/// &param nCostMoveDiag is the cost of moving verticaly and horizontaly at the same time. Usually you should use 14. Its not used if nDirCount is 4.
	/// &param nCostChangeDir is the cost of changing direction. Use 0 if there is no cost involve in changing direction. Usually you should use 10.
	/// &param nDirCount is used to specify if the algo should look at diagonals possibilities (8) or only the minimal ones (4).
	/// &param nCollisionMask is a mask applied to your Physical Map to find collision. Usefull if you want to store more info in you map. By default the mask should be 0xFFFFFFFF.
	//--------------------------------------------------------------------------
	public void PathFinding_Init (int nMapWidth, int nMapHeight, byte[] pPhysicalMap, int nCostMove, int nCostMoveDiag, int nCostChangeDir, int nDirCount, int nCollisionMask)
	{
		Utils.DBG_PrintStackTrace((nDirCount == 4) || (nDirCount == 8), "PathFinding_Init nDirCount should be 4 or 8.");

		m_nMapW				= nMapWidth;
		m_nMapH				= nMapHeight;

		m_pPhysMap			= pPhysicalMap;

		m_nPhysMapMask      = nCollisionMask;

		m_nCostMove			= nCostMove;
		m_nCostMoveDiag		= nCostMoveDiag;
		m_nCostChangeDir	= nCostChangeDir;

		//4 or 8
		m_nUseDirectionCount = nDirCount;

		m_nodeParent		= new short[DevConfig.pathfinding_MaxNode];
		m_nodePrev			= new short[DevConfig.pathfinding_MaxNode];
		m_nodeNext			= new short[DevConfig.pathfinding_MaxNode];
		m_nodeG				= new short[DevConfig.pathfinding_MaxNode];
		m_nodeH				= new short[DevConfig.pathfinding_MaxNode];

		// init sorted list
		m_openedSortedList	= -1;

		// allocate path array.
		m_path				= new short[DevConfig.pathfinding_MaxNode];
		m_pathIdx			= -1;

		m_init 				= true;
	}

	//--------------------------------------------------------------------------
	/// PathFinding_Exec is the heart of the engine, the search is done in this function. The search will be completed with one call.
	/// &param start_x Starting tile's x coordinate.
	/// &param start_y Starting tile's y coordinate.
	/// &param start_dir Starting direction of the entity that will be moving.
	/// &param end_x Ending tile's x coordinate.
	/// &param end_y Ending tile's y coordinate.
	//--------------------------------------------------------------------------
	public void PathFinding_Exec (int start_x, int start_y, int start_dir, int end_x, int end_y)
	{
		PathFinding_Exec(start_x, start_y, start_dir, end_x, end_y, 0, 0, m_nMapW, m_nMapH);
	}

	//--------------------------------------------------------------------------
	/// PathFinding_Exec is the heart of the engine, the search is done in this function. The search will be completed with one call.
	/// This variant will search only the given subset of the map. This can give performance gains when you know the path will
	/// be restricted to a given area within the total map.
	/// &param start_x Starting tile's x coordinate.
	/// &param start_y Starting tile's y coordinate.
	/// &param start_dir Starting direction of the entity that will be moving.
	/// &param end_x Ending tile's x coordinate.
	/// &param end_y Ending tile's y coordinate.
	/// &param search_x Tile x coordinate of the area in the map to search.
	/// &param search_y Tile y coordinate of the area in the map to search.
	/// &param search_w Tile width of the area in the map to search (must be greater than search_x).
	/// &param search_h Tile height of the area in the map to search (must be greater than search_y).
	//--------------------------------------------------------------------------
	public void PathFinding_Exec (int start_x, int start_y, int start_dir, int end_x, int end_y, int search_x, int search_y, int search_w, int search_h)
	{
		Utils.DBG_PrintStackTrace(m_init, "GLLibPathFinding not initialized");

		int	dir = start_dir;

		if (DevConfig.pathfinding_Debug)
		{
			Utils.Dbg("------------- execPathFinding -------------");
			Utils.Dbg("execPathFinding from (" + start_x + ", " + start_y +") to (" + end_x + ", " + end_y + ")");
			Utils.Dbg("  searching sub-area (" + search_x + ", " + search_y +") to (" + (search_x + search_w) + ", " + (search_y + search_h) + ")");
		}

		for (int i = 0; i < DevConfig.pathfinding_MaxNode; i++)
		{
			// reset nodes.
			m_nodeParent[i]	= -1;
			m_nodePrev[i]	= -1;
			m_nodeNext[i]	= -1;
			m_nodeG[i]		= 0;
			m_nodeH[i]		= 0;

			// reset path.
			m_path[i] = 0;
		}

		// reset sorted opened list.
		m_openedSortedList = -1;

		int a_idx, n_idx, p_idx;

		// get start node.
		// pack the x and y starting coordinates according to the search area
		n_idx = (((start_y - search_y)*search_w)+(start_x - search_x));

		int node_x;
		int	node_y;
		int	adj_x;
		int	adj_y;
		int	dx;
		int	dy;
		int	g;
		int	h;

		int search_end_x = end_x - search_x;
		int search_end_y = end_y - search_y;

		int nMoveCost;
		int nDirCost;

		int nodesVisited = 0;

		while (n_idx != -1)
		{
			// remove it from opened list.
			listRem(n_idx);

			// Must save this cost! Otherwise we will assume cost to previous node is always -1!!! [MMZ]
			short gCost_n_idx = m_nodeG[n_idx];

			// add it to closed list.
			m_nodeG[n_idx] = -1;
			m_nodeH[n_idx] = -1;

			// unpack the current node's position (according to the search area)
			node_x = (n_idx%search_w);
			node_y = (n_idx/search_w);

			// check if we are on destination node.
			if (node_x == search_end_x && node_y == search_end_y)
			{
				if (DevConfig.pathfinding_Debug)
				{
					Utils.Dbg("a path has been found");
				}
				break;
			}

			if (DevConfig.pathfinding_Debug)
			{
				nodesVisited++;

				Utils.Dbg("current node[" + n_idx + "] is {" +
						(node_x + search_x) + " (" + node_x + " + " + search_x + "), " +
						(node_y + search_y) + " (" + node_y + " + " + search_y + ")}");
			}

			// look for adjacent nodes.
			for (int i = 0; i < m_nUseDirectionCount; i++)
			{
				// compute adjacent node's position.
				adj_x = kDirPrecalc[(i<<1)+0];
				adj_y = kDirPrecalc[(i<<1)+1];

				if((adj_x != 0) && (adj_y != 0))
				{
					nMoveCost = m_nCostMoveDiag;
				}
				else
				{
					nMoveCost = m_nCostMove;
				}

				adj_x += node_x;
				adj_y += node_y;

				if (DevConfig.pathfinding_Debug)
				{
					Utils.Dbg("position is {" +
							(adj_x + search_x) + " (" + adj_x + " + " + search_x + "), " +
							(adj_y + search_y) + " (" + adj_y + " + " + search_y + ")}");
				}

				// exclude positions that are located outside the search area
				if (adj_x < 0 || adj_x >= search_w)
				{
					if (DevConfig.pathfinding_Debug)
					{
						Utils.Dbg("x position is not in the map");
					}
					continue;
				}

				if (adj_y < 0 || adj_y >= search_h)
				{
					if (DevConfig.pathfinding_Debug)
					{
						Utils.Dbg("y position is not in the map");
					}
					continue;
				}


				if ( i>=4 )
				{
					int x = n_idx%search_w;
					int y = n_idx/search_w;
					// diagonal : can`t cross wall neighbour
					if ( i==kDirUpLeft )
					{
						if ((m_pPhysMap[(((y + search_y)*m_nMapW)+(x - 1 + search_x))] & m_nPhysMapMask) != 0) // left
							continue;
						if ((m_pPhysMap[(((y - 1 + search_y)*m_nMapW)+(x + search_x))] & m_nPhysMapMask) != 0) // up
							continue;
					}
					else if ( i==kDirUpRight )
					{
						if ((m_pPhysMap[(((y + search_y)*m_nMapW)+(x + 1 + search_x))] & m_nPhysMapMask) != 0) // right
							continue;
						if ((m_pPhysMap[(((y - 1 + search_y)*m_nMapW)+(x + search_x))] & m_nPhysMapMask) != 0) // up
							continue;
					}
					else if ( i==kDirDownLeft )
					{
						if ((m_pPhysMap[(((y + search_y)*m_nMapW)+(x - 1 + search_x))] & m_nPhysMapMask) != 0) // left
							continue;
						if ((m_pPhysMap[(((y + 1 + search_y)*m_nMapW)+(x + search_x))] & m_nPhysMapMask) != 0) // down
							continue;
					}
					else if ( i==kDirDownRight )
					{
						if ((m_pPhysMap[(((y + search_y)*m_nMapW)+(x + 1 + search_x))] & m_nPhysMapMask) != 0) // right
							continue;
						if ((m_pPhysMap[(((y + 1 + search_y)*m_nMapW)+(x + search_x))] & m_nPhysMapMask) != 0) // up
							continue;
					}
				}


				// get the node.
				a_idx = ((adj_y*search_w)+adj_x);

				if (DevConfig.pathfinding_Debug)
				{
					Utils.Dbg("\tadjacent node[" + a_idx + "] is {" +
							(adj_x + search_x) + " (" + adj_x + " + " + search_x + "), " +
							(adj_y + search_y) + " (" + adj_y + " + " + search_y + ")}");
				}

				if((m_pPhysMap[(((adj_y + search_y)*m_nMapW)+(adj_x + search_x))] & m_nPhysMapMask) != 0)
				{
					if (DevConfig.pathfinding_Debug)
					{
						Utils.Dbg("\tnode is a wall");
					}

					// not available
					continue;
				}


				// exclude it if it has been closed.
				if (m_nodeG[a_idx] == -1)
				{
					if (DevConfig.pathfinding_Debug)
					{
						Utils.Dbg("\tnode has been already checked");
					}
					continue;
				}

				nDirCost = (dir == i ? 0 : m_nCostChangeDir);


				// compute move total cost.
				//g = m_nodeG[n_idx] + nMoveCost + nDirCost;   <-- m_nodeG[n_idx] is -1!!!!
				g = gCost_n_idx + nMoveCost + nDirCost;

				// compute heuristic.
				dx = Math.abs(adj_x - search_end_x);
				dy = Math.abs(adj_y - search_end_y);

				if (m_nUseDirectionCount == 4)
				{
					h = m_nCostMove * (dx+dy);
				}
				else
				{
					if (dx > dy)
						h = (m_nCostMoveDiag * dy) + (m_nCostMove * (dx-dy));
					else
						h = (m_nCostMoveDiag * dx) + (m_nCostMove * (dy-dx));
				}
				// compute heuristic. DONE


				// if the node is not present in the opened list,
				if (m_nodePrev[a_idx] == -1 && m_nodeNext[a_idx] == -1 && m_openedSortedList != a_idx)
				{
					// keep reference on parent n.
					m_nodeParent[a_idx] = (short)n_idx;

					// save current values.
					m_nodeG[a_idx] = (short)g;
					m_nodeH[a_idx] = (short)h;

					if (DevConfig.pathfinding_Debug)
					{
						Utils.Dbg("\tadd adjacent node to open list (score : " + (g+h) + ")");
					}

					// add it to the opened list.
					listAdd(a_idx);
				}
				// if the node is present but on a longer path.
				else if (m_nodeG[a_idx] > g)
				{
					// keep reference on new parent n.
					m_nodeParent[a_idx] = (short)n_idx;

					// save new g.
					m_nodeG[a_idx] = (short)g;

					if (DevConfig.pathfinding_Debug)
					{
						Utils.Dbg("\trelink adjacent node to current node");
					}

					// remove and add (in order to sort).
					listRem(a_idx);
					listAdd(a_idx);
				}
			} // for (int i = 0; i < m_nUseDirectionCount; i++)

			// change current node.
			n_idx = m_openedSortedList;

			if (DevConfig.pathfinding_Debug)
			{
				Utils.Dbg("change current node to : " + n_idx + " {" +
						((n_idx%search_w) + search_x) + "(" + (n_idx%search_w) + " + " + search_x + ")," +
						((n_idx/search_w) + search_y) + "(" + (n_idx/search_w) + " + " + search_y + ")}");
			}

			if (n_idx != -1)
			{
				// change current direction.
				p_idx = m_nodeParent[n_idx];

				// TODO : if (m_nUseDirectionCount == 8)   (DIAGONAL case)	"dir" can be kDirUpLeft, kDirUpRight, kDirDownLeft or kDirDownRight...
				if ((n_idx%search_w) != (p_idx%search_w))
				{
					if ((n_idx%search_w) > (p_idx%search_w))
					{
						dir = kDirRight;
					}
					else
					{
						dir = kDirLeft;
					}
				}
				else
				{
					if ((n_idx/search_w) != (p_idx/search_w))
					{
						dir = kDirDown;
					}
					else
					{
						dir = kDirUp;
					}
				}

				if (DevConfig.pathfinding_Debug)
				{
					Utils.Dbg("new node[" + n_idx + "] (score : " + (m_nodeG[n_idx]+m_nodeH[n_idx]) + ") is {" +
							((n_idx%search_w) + search_x) + " (" + (n_idx%search_w) + " + " + search_x + "), " +
							((n_idx/search_w) + search_y) + " (" + (n_idx/search_w) + " + " + search_y + ")}");
				}
			}
		} // while (n_idx != -1)

		// check if we have a valid node
		if (n_idx == -1)
		{
			if (DevConfig.pathfinding_Debug)
			{
				Utils.Dbg("opened list is empty... there is no path");
				Utils.Dbg("-------------------------------------------");
			}
			m_pathIdx = -1;
			return;
		}

		// store path to array
		m_pathIdx = 0;
		while (n_idx != -1)
		{
			// store path.
			int x = (n_idx%search_w) + search_x;
			int y = (n_idx/search_w) + search_y;
			// always store the path position as the final position in the map (not as a position in the search area)
			m_path[m_pathIdx++] = (short)(((y)*m_nMapW)+(x));

			// get next node.
			n_idx = m_nodeParent[n_idx];
		}
		m_pathIdx -= 1;

		if (DevConfig.pathfinding_Debug)
		{
			Utils.Dbg("-------------------------------------------");
			Utils.Dbg("Path was found with " + nodesVisited + " nodes visited");

			for (int i = m_pathIdx; i >= 0; i--)
			{
				int pos = m_path[i];
				int x = ((pos)%m_nMapW);
				int y = ((pos)/m_nMapW);

				Utils.Dbg("node [" + i + "]\t= (" + x + ", " + y + ")");
			}
			Utils.Dbg("-------------------------------------------");
		}
	}


	//--------------------------------------------------------------------------
	/// PathFinding_GetPathLength will give you the path length that was found by the previous call to PathFinding_Exec.
	/// &return The current path length. 0 if no path was found.
	/// &note You need to call PathFinding_Exec once before calling this fonction.
	//--------------------------------------------------------------------------
	public int PathFinding_GetPathLength()
	{
		Utils.DBG_PrintStackTrace(m_init, "GLLibPathFinding not initialized");
		return m_pathIdx + 1;
	}


	//--------------------------------------------------------------------------
	/// PathFinding_GetPathPosition will give you the position of the path node in array index i.e. ((y*width) + x) so that you can access you array without computing the array pos.
	/// &param nIndex The path node you want to query.
	/// &return The path node position in your collision map.
	/// &note You need to call PathFinding_Exec once before calling this fonction.
	/// &note The result will be backward, going from the end to the bening, so PathFinding_GetPathPosition(0) start from the end.
	//--------------------------------------------------------------------------
	public int PathFinding_GetPathPosition(int nIndex)
	{
		Utils.DBG_PrintStackTrace(m_init, "GLLibPathFinding not initialized");
		return m_path[nIndex];
	}

	//--------------------------------------------------------------------------
	/// PathFinding_GetPathPositionX will give you the x position of the path node you are querying.
	/// &param nIndex The path node you want to query.
	/// &return The X position your collision map.
	/// &note You need to call PathFinding_Exec once before calling this fonction.
	//--------------------------------------------------------------------------
	public int PathFinding_GetPathPositionX(int nIndex)
	{
		Utils.DBG_PrintStackTrace(m_init, "GLLibPathFinding not initialized");
		return ((m_path[nIndex])%m_nMapW);
	}

	//--------------------------------------------------------------------------
	/// PathFinding_GetPathPositionY will give you the y position of the path node you are querying.
	/// &param nIndex The path node you want to query.
	/// &return The Y position your collision map.
	/// &note You need to call PathFinding_Exec once before calling this fonction.
	//--------------------------------------------------------------------------
	public int PathFinding_GetPathPositionY(int nIndex)
	{
		Utils.DBG_PrintStackTrace(m_init, "GLLibPathFinding not initialized");
		return ((m_path[nIndex])/m_nMapW);
	}


	//--------------------------------------------------------------------------
	/// PathFinding_Free will free all of the internal buffers and arrays.
	/// &param bKeepLastPath If true, will keep the last path found array. If false, it will be freed also, and calls to query path results will fail.
	//--------------------------------------------------------------------------
	public void PathFinding_Free (boolean bKeepLastPath)
	{
		m_init 			= false;

		m_nodeParent 	= null;
		m_nodePrev		= null;
		m_nodeNext		= null;
		m_nodeG			= null;
		m_nodeH			= null;

		m_pPhysMap		= null;

		if(!bKeepLastPath)
		{
			m_pathIdx	= -1;
			m_path 		= null;
		}


		GLLib.Gc();
	}

	//--------------------------------------------------------------------------
	/// listAdd
	///
	/// &param e_idx element's index to add to the list.
	//--------------------------------------------------------------------------
	private void listAdd (int e_idx)
	{
		// if list is empty.
		if (m_openedSortedList == -1)
		{
			// insert elm.
			m_openedSortedList = e_idx;
			return;
		}

		// compute element's value.
		int elm_value = m_nodeG[e_idx] + m_nodeH[e_idx];

		// insert element in list
		for (int n_idx = m_openedSortedList; n_idx != -1; n_idx = m_nodeNext[n_idx])
		{
			int cur_value = m_nodeG[n_idx] + m_nodeH[n_idx];

			if (elm_value < cur_value)
			{
				// insert element.
				if (m_nodePrev[n_idx] == -1)
				{
					m_openedSortedList = e_idx;
				}
				else
				{
					int p_idx = m_nodePrev[n_idx];
					m_nodeNext[p_idx] = (short)e_idx;
				}

				m_nodePrev[e_idx] = m_nodePrev[n_idx];
				m_nodeNext[e_idx] = (short)n_idx;
				m_nodePrev[n_idx] = (short)e_idx;

//				listDisplay();
				return;
			}
			else if (m_nodeNext[n_idx] == -1)
			{
				m_nodeNext[n_idx] = (short)e_idx;
				m_nodePrev[e_idx] = (short)n_idx;

//				listDisplay();
				return;
			}
		}
	}

	//--------------------------------------------------------------------------
	/// listAdd
	///
	/// &param e_idx element's index to remove from the list.
	//--------------------------------------------------------------------------
	private void listRem (int e_idx)
	{
		// if next element exists change its prev reference.
		if (m_nodeNext[e_idx] != -1)
		{
			int n_idx = m_nodeNext[e_idx];
			m_nodePrev[n_idx] = m_nodePrev[e_idx];
		}

		if (m_openedSortedList == e_idx)
		{
			m_openedSortedList = m_nodeNext[e_idx];
		}
		else if (m_nodePrev[e_idx] != -1)
		{
			int p_idx = m_nodePrev[e_idx];
			m_nodeNext[p_idx] = m_nodeNext[e_idx];
		}

		m_nodePrev[e_idx] = -1;
		m_nodeNext[e_idx] = -1;
	}

	//--------------------------------------------------------------------------
	/// listDisplay
	//--------------------------------------------------------------------------
	private void listDisplay ()
	{
		if(true)
		{
			int n_idx = m_openedSortedList;

			Utils.Dbg("************* listDisplay *************");

			while (n_idx != -1)
			{
				Utils.Dbg("elm index["+n_idx+"] (" + ((n_idx)%m_nMapW) + ", " + ((n_idx)/m_nMapW) + ") with value = " + (m_nodeG[n_idx] + m_nodeH[n_idx]));
				n_idx = m_nodeNext[n_idx];
			}

			Utils.Dbg("***************************************");
		}
	}
}

/// &}
