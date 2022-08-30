import java.util.Random;

public class MyField {

    public Square[][] field;
    public int height;
    public int wight;
    int bombs;
    int counter;

    public MyField(int height, int wight){

        this.field = new Square[height][wight];
        this.height = height;
        this.wight = wight;
        bombs = (height*wight)/8;
        counter=height*wight;

        for (int i=0; i<height; i++){
            for (int j=0; j<wight; j++){
                field[i][j]=new Square();
            }
        }

        fillByBombs(field,height,wight);
        fillField(field,height,wight);

        for (int i=0; i<height; i++){
            for (int j=0; j<wight; j++){
                System.out.print(field[i][j].first + "  ");
            }
            System.out.println();
        }
    }

    private void fillByBombs (Square[][] field, int height, int wight){
        Random random = new Random();
        int x;
        int y;
        while (bombs != 0){
            x=random.nextInt(height);
            y=random.nextInt(wight);
            if(field[x][y].first != -1){
                field[x][y].setFirst(-1);
                bombs--;
            }
        }
        bombs = (height*wight)/8;
    }

    private void fillField (Square[][] field, int height, int wight){
        for (int i=0; i<height; i++){
            for (int j=0; j<wight; j++){
                if(field[i][j].first == -1){
                    for(int k=-1; k<2 ;k++){
                        for(int l=-1; l<2; l++){
                            if(i+k>=0 && i+k<height && j+l>=0 && j+l<wight){
                                if(field[i+k][j+l].first != -1){
                                    field[i+k][j+l].setFirst(field[i+k][j+l].first+1);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public int openSquare (int x, int y){
        if(x>=0 && x<this.height && y>=0 && y<wight) {
            if(this.getFirstByIndex(x,y) == -1 && this.getSecondByIndex(x,y) == 0){
                field[x][y].setSecond(-2);
                    for (int i = 0; i < height; i++) {
                        for (int j = 0; j < wight; j++) {
                            if(this.getFirstByIndex(i,j) == -1 && this.getSecondByIndex(i,j) == 0){
                                field[i][j].setSecond(-1);
                            }
                        }
                    }
                return -1;
            }
            if(this.getSecondByIndex(x,y) == 0) {
                field[x][y].setSecond(-1);
                counter--;
                if (this.getFirstByIndex(x, y) == 0) {
                    for (int i = -1; i < 2; i++) {
                        for (int j = -1; j < 2; j++) {
                            this.openSquare(x + i, y + j);
                        }
                    }
                }
            }
        }
        if(counter==bombs){
            return 1;
        }
        return 0;
    }

    public int getFirstByIndex (int i, int j){
        return this.field[i][j].getFirst();
    }

    public int getSecondByIndex (int i, int j){
        return this.field[i][j].getSecond();
    }

    public void setFlag(int x,int y){
        switch (field[x][y].second){
            case 0:
                field[x][y].setSecond(1);
                break;
            case 1:
                field[x][y].setSecond(0);
                break;
            default:
                break;
        }
    }
}


class Square{
    protected int first;
    protected int second;

    Square(int x,int y){
        this.first=x;
        this.second=y;
    }

    Square(){
        this.first=0;
        this.second=0;
    }

    protected void setFirst(int x){
        this.first=x;
    }

    protected void setSecond(int y){
        this.second=y;
    }

    public int getFirst(){
        return first;
    }

    public int getSecond(){
        return second;
    }
}
