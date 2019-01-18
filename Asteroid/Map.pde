class Map {
  // Attributes ----------------------------------
  int starCount;
  int metCount;
  Starfield stF;
  Meteor[] meteors;


  // Constructor ---------------------------------
  Map(int starCount, int metCount) {

    meteors = new Meteor[metCount];
    this.starCount = starCount;
    stF = new Starfield(this.starCount);
    
    for(int i = 0; i < metCount;i++) {
      meteors[i] = new Meteor(1);
    }
    
    

  }

  // Methods -------------------------------------

  void bgGraphic() {

    stF.show();

    for(int i = 0; i < meteors.length; i++) {
      meteors[i].show();
      meteors[i].update();
    }

  }

  int getStarCount() {
    int starCount = this.starCount;
    return starCount;
  }
}
