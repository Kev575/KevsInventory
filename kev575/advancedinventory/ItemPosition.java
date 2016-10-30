package kev575.advancedinventory;

/**
 * @author Kev575
 */
public class ItemPosition {

	private final int row, column;

	/**
	 * @param row
	 *            the row
	 * @param column
	 *            the column
	 */
	public ItemPosition(int row, int column) {
		this.row = row;
		this.column = column;
	}

	/**
	 * @return the row
	 */
	public int getRow() {
		return row;
	}

	/**
	 * @return the column
	 */
	public int getColumn() {
		return column;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof ItemPosition)) {
			return false;
		}
		ItemPosition pos = (ItemPosition) obj;
		return pos.getColumn() == getColumn() && pos.getRow() == getRow();
	}

}
