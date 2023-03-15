package model;

import java.util.Random;

public class ForgetfulTile extends Tile{

	public ForgetfulTile(int idTile) {
		super(idTile);
		// TODO Auto-generated constructor stub
	}
	
	public int[] changePlace(int[] listForget) {
		// find the place
		int index=findIndex(listForget,super.getId());
		int new_tile;
		do {
			// choose random number
			new_tile=getRandomNumberUsingNextInt(1,65);
		}while(findIndex(listForget,new_tile)!=-1);   // Choose again if the place is problematic
		listForget[index]=new_tile; // change place of the Forget Tile
		return (listForget);
	}

	
	// function take number and list , return the place of number in the list
	public static int findIndex(int arr[], int t)
    {
        // if array is Null
        if (arr == null) {
            return -1;
        }
        // find length of array
        int len = arr.length;
        int i = 0;
        // traverse in the array
        while (i < len) {
            // if the i-th element is t
            // then return the index
            if (arr[i] == t) {
                return i;
            }
            else {
                i = i + 1;
            }
        }
        return -1;
    }
	
	
	// function take Number range and return random number
		public int getRandomNumberUsingNextInt(int min, int max) {
	   	    Random random = new Random();
	   	    return random.nextInt(max - min) + min;
	   	}
}
