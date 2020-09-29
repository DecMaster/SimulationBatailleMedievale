package data;

import java.io.FileReader;
import java.io.IOException;

public class Terrain {
	public static enum Type {
		NORMAL,
		SWAMP,
		FOREST,
		HILL,
		HOUSE;
	}
	
	private static int height;
	private static int width;
	private static Type[] terrain;
	
	
	public Terrain(String path) {
		importFromFile(path);
	}
	
	
	public static Type getTerrain(int w, int h) {
			//Comportement torique
		int nh = h % height;
		int nw = w % width;
		return terrain[width * nh + nw];
	}
	
	public void importFromFile(String path) {
			// Les fichier doivent avoir un nom type NomDeTerrain_25x9 par exemple

		FileReader inputStream = null;
		try {
			inputStream = new FileReader(path);
			char c;
			int i = 0;
			
				//Création d'un terrain de la bonne taille
			String line = "";
			while ((c = (char) inputStream.read()) != (char) '\n')

                if (c != '\r')
                    line += c;
			width = Integer.parseInt(line);
			line = "";
			while ((c = (char) inputStream.read()) != (char) '\n')
                if (c != '\r')
                    line += c;

			height = Integer.parseInt(line);
			terrain = new Type[height * width];
			System.out.println("Création Terr " + width + "*" + height);
				//Màj des variables globales
			Constantes.GRID_SIZE_X = width;
			Constantes.GRID_SIZE_Y = height;
			
            while ((c = (char) inputStream.read()) != (char) -1 ) {
            	switch (c) {
				case '\n': case '\r': // \r pour les haindeux qui utilisent Windows
					continue;
				case '#':
					terrain[i++] = Type.NORMAL;
					break;
				case 'Y':
					terrain[i++] = Type.FOREST;
					break;
				case '~':
					terrain[i++] = Type.SWAMP;
					break;
				case '^':
					terrain[i++] = Type.HILL;
					break;
				case 'h':
					terrain[i++] = Type.HOUSE;
					break;
				default:
					// System.out.println("Default + " + "\'" + c + "\'");
					break;
				}
            }
			
		} catch (Exception e) {
			System.out.print("Erreur lecture fichier " + path);
			e.printStackTrace();
			System.exit(0);
		} finally {
            if (inputStream != null) {
                try {
					inputStream.close();
				} catch (IOException e) {
					System.out.println("Erreur fermeture fichier");
					e.printStackTrace();
				}
            }
        }	
	}
	
	public void showTerrain() {
		for (int h = 0 ; h < height ; ++h) {
			for (int w = 0 ; w < width ; ++w) {
				switch (getTerrain(w, h)) {
				case NORMAL:
					System.out.print('.');
					break;
				case FOREST:
					System.out.print('Y');
					break;
				case SWAMP:
					System.out.print('~');
					break;
				case HILL:
					System.out.print('^');
					break;
				case HOUSE:
					System.out.print('h');
					break;
				default:
					break;
				}
			}
			System.out.print('\n');
		}
	}

}