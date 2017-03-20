        if (matrice[x-1][y]==0 && matrice[x+1][y]==0  && matrice[x][y-1]==0 && matrice[x][y+1]==255) matrice[x][y]=255;
        if (matrice[x-1][y]==255 && matrice[x+1][y]==0  && matrice[x][y-1]==0 && matrice[x][y+1]==0) matrice[x][y]=255;
        if (matrice[x-1][y]==0 && matrice[x+1][y]==0  && matrice[x][y-1]==255 && matrice[x][y+1]==0) matrice[x][y]=255;
        if (matrice[x-1][y]==0 && matrice[x+1][y]==255  && matrice[x][y-1]==0 && matrice[x][y+1]==0) matrice[x][y]=255;
 
        if (matrice[x-1][y-1]==255 && matrice[x-1][y]==255  && matrice[x-1][y+1]==255 && matrice[x][y+1]==255 && matrice[x+1][y+1]==0 && matrice[x+1][y]==0 && matrice[x][y-1]==255)   matrice[x][y]=255;
        if (matrice[x-1][y-1]==255 && matrice[x-1][y]==255  && matrice[x-1][y+1]==255 && matrice[x][y+1]==255 && matrice[x+1][y]==0   && matrice[x+1][y-1]==0 && matrice[x][y-1]==255) matrice[x][y]=255;
        if (matrice[x-1][y-1]==0 && matrice[x-1][y]==0   && matrice[x][y+1]==255   && matrice[x+1][y+1]==255 && matrice[x+1][y]==255 && matrice[x+1][y-1]==255 && matrice[x][y-1]==255) matrice[x][y]=255;
        if (matrice[x-1][y]==0   && matrice[x-1][y+1]==0 && matrice[x][y+1]==255   && matrice[x+1][y+1]==255 && matrice[x+1][y]==255 && matrice[x+1][y-1]==255 && matrice[x][y-1]==255) matrice[x][y]=255;
 
        if (matrice[x-1][y-1]==255 && matrice[x-1][y]==255   && matrice[x][y+1]==0   && matrice[x+1][y+1]==0 && matrice[x+1][y]==255   && matrice[x+1][y-1]==255 && matrice[x][y-1]==255) matrice[x][y]=255;
        if (matrice[x-1][y-1]==255 && matrice[x-1][y]==255   && matrice[x-1][y+1]==0 && matrice[x][y+1]==0  && matrice[x+1][y]==255   && matrice[x+1][y-1]==255 && matrice[x][y-1]==255) matrice[x][y]=255;
        if (matrice[x-1][y]==255   && matrice[x-1][y+1]==255 && matrice[x][y+1]==255   && matrice[x+1][y+1]==255 && matrice[x+1][y]==255   && matrice[x+1][y-1]==0 && matrice[x][y-1]==0) matrice[x][y]=255;
        if (matrice[x-1][y-1]==0 && matrice[x-1][y]==255   && matrice[x-1][y+1]==255 && matrice[x][y+1]==255   && matrice[x+1][y+1]==255 && matrice[x+1][y]==255   && matrice[x][y-1]==0) matrice[x][y]=255;
 
        if (matrice[x-1][y]==255   && matrice[x-1][y+1]==255 && matrice[x][y+1]==255 && matrice[x+1][y]==0   && matrice[x][y-1]==0) matrice[x][y]=255;
        if (matrice[x-1][y-1]==255 && matrice[x-1][y]==255 && matrice[x][y+1]==0 && matrice[x+1][y]==0  && matrice[x][y-1]==255) matrice[x][y]=255;
        if (matrice[x-1][y]==0   && matrice[x][y+1]==0   && matrice[x+1][y]==255   && matrice[x+1][y-1]==255 && matrice[x][y-1]==255) matrice[x][y]=255;
        if (matrice[x-1][y]==0   && matrice[x][y+1]==255   && matrice[x+1][y+1]==255 && matrice[x+1][y]==255   && matrice[x][y-1]==0) matrice[x][y]=255;
        