const app = angular.module('myApp', ['toastr']);

app.controller('BreaktruController', (toastr, $scope, $http) => {
  $scope.rows = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K'];
  $scope.columns = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11];

  let player = "";
  let AI = "";
  let playersTurn = "";
  let remainingMoves = 0;
  let turnCount = 0;
  let count = 0;
  $scope.goldPlayer = "";
  $scope.silverPlayer = "";
  $scope.object = { turn: "" };
  $scope.object = { count: null };
  let logs = {};
  $scope.object = { logs: {} };

  var pos = [];

  //Toggle of selected / destination markers on board
  $scope.getInitial = function (col, row, idx, idxp) {

    pos.push(idx, idxp);

    //ng-class '.initial' or '.destination' applied to selected element
    $scope.initialIdx = [pos[0], pos[1]];
    $scope.destinationIdx = [pos[2], pos[3]];

    if (pos.length >= 4) pos.splice(0);

    $scope.move = function () {
      console.log("move");
      var initial = angular.element('.initial');
      var destination = angular.element('.destination');
      console.log(initial[0].className);
      console.log("destination " + destination[0].className);

      // Piece class e.g. 'WP'
      var piece = initial[0].className.split(' ')[3];
      console.log("piece " + piece);

      var checkPos = destination[0].className.split(' ');
      console.log("check " + checkPos);
      console.log(initial[0].className.split(' ')[3].toString().charAt(0));

      var moveObj = new Object();
      moveObj.from = initial[0].className.split(' ')[2];
      moveObj.to = destination[0].className.split(' ')[2];
      moveObj.player = initial[0].className.split(' ')[3].toString().charAt(0);
      moveObj.remainingMoves = remainingMoves;


      var jsonMove = JSON.stringify(moveObj);
      let headers = {
        headers: {
          "Content-Type": "application/json"
        }
      };


      $http.post("/api/move", moveObj, headers).then((response) => {
        console.log("response: " + JSON.stringify(response));
        console.log("response message " + response.data.result);
        if (response.data.result == ("GAME WON by " + moveObj.player)) {
          remainingMoves = -100;
          toastr.success("GAME WON by " + moveObj.player)
        }
        if (response.data.result == ("ILLEGAL MOVE")) {
          remainingMoves = -100;
          toastr.error("ILLEGAL MOVE BY: " + moveObj.player);
        }
      });

      //TODO: BLKWHT doesn't work!!!
      var blkWht = checkPos[3].substring(0, 1);
      console.log("blkWht " + blkWht);
      //check if position is white or black piece already
      if (blkWht === 'S' || blkWht === 'G') {

        //remove the overtaken piece class
        destination.removeClass(checkPos[checkPos.length - 2].substring(0, 2));
        //            toastr.success('Wow! Nice move!')
      }

      //Add class to overtaken position
      destination.addClass(initial[0].className.split(' ')[3]);

      //reset square
      initial.removeClass(piece);
      console.log(initial);

      //reset ng-class values
      this.initialIdx = false;
      this.destinationIdx = false;

      var from = new Object();
      from.col = letterToNumber(initial[0].className.split(' ')[2].toString().charAt(0));
      from.row = parseInt(initial[0].className.split(' ')[2].toString().substring(1));
      var to = new Object();
      to.col = letterToNumber(destination[0].className.split(' ')[2].toString().charAt(0));
      to.row = parseInt(destination[0].className.split(' ')[2].toString().substring(1));
      logs[count] = 'Turn ' + turnCount + '| ' + playersTurn + ' moves ' +
      moveObj.from + ' to ' + moveObj.to;
      count++;
      //TODO: UPDATE LOG FUNCTIONALITY!!
      console.log(logs);
      $scope.object.logs = logs;
      console.log($scope.object.logs);
      if ((from.row == to.row - 1) && (from.col == to.col - 1) ||
        (from.row == to.row - 1) && (from.col == to.col + 1) ||
        (from.row == to.row + 1) && (from.col == to.col - 1) ||
        (from.row == to.row + 1) && (from.col == to.col + 1)) {
        console.log("capture");
        remainingMoves = remainingMoves - 2;
      } else if (piece == "GFS" && playersTurn == "G") {
        remainingMoves = remainingMoves - 2;
        console.log(remainingMoves);

      } else {
        remainingMoves--;
      }
      console.log(remainingMoves);
      if (remainingMoves == 0) {
        turnCount++;
        if (playersTurn == "G") {
          playersTurn = "S";
          $scope.object = { turn: "S" };
          $scope.object.count = turnCount;
          $scope.object.logs = logs;
          console.log($scope.object.logs);
          remainingMoves = 2;
        } else {
          playersTurn = "G";
          $scope.object = { turn: "G" };
          $scope.object.count = turnCount;
          $scope.object.logs = logs;
          console.log($scope.object.logs);
          remainingMoves = 2;
        }
      }
    }
  }

  $scope.init = function () {
    console.log('init');
    playersTurn = "G";
    $http.get("/api/init").then((response) => { });
    var squares = document.getElementsByClassName("chess-sq");
    for (let i = 0; i < squares.length; i++) {
      if (squares[i].classList.contains("SE")) {
        squares[i].classList.remove("SE")
      }
      if (squares[i].classList.contains("GE")) {
        squares[i].classList.remove("GE")
      }
      if (squares[i].classList.contains("GFS")) {
        squares[i].classList.remove("GFS")
      }
      let row = squares[i].classList[2].toString().charAt(0);
      let column = squares[i].classList[2].toString().slice(1);
      let newClass = $scope.piece(row, column);
      if (newClass) {
        squares[i].classList.add(newClass);
      }
    }
  }

  $scope.gold = function () {
    player = "G";
    AI = "S";
    remainingMoves = 2;
    $scope.goldPlayer = "Human";
    $scope.silverPlayer = "AI";
    $scope.object = { turn: "G" };
    $scope.object.count = 1;
    turnCount = 1;
  }

  $scope.textMove = () => {
    if ($scope.text.length === 4) {
      var piece = this.text.substring(0, 2).toUpperCase();
      var position = this.text.substring(2, 4).toUpperCase();
      var init = angular.element(`.${position}`);
      init.removeClass(piece);
    }
    if ($scope.dest.length === 2) {
      var position = this.dest.substring(0, 2).toUpperCase();
      var destination = angular.element(`.${position}`);
      destination.addClass(piece);
    }

  };

  // This function is called inside of the ng-class object, for each div that is repeated,
  // It sets up the initial pieces classes for each side.
  $scope.piece = (row, col) => {
    var sqr = row + col;

    //        //white
    //        if (col == 2) return 'WP';
    //        if (sqr == 'A1' || sqr == 'H1') return 'WR';
    //        if (sqr == 'B1' || sqr == 'G1') return 'WH';
    //        if (sqr == 'C1' || sqr == 'F1') return 'WB';
    //        if (sqr == 'D1') return 'WQ';
    //        if (sqr == 'E1') return 'WK';
    if (sqr == 'B4' || sqr == 'B5' || sqr == 'B6' || sqr == 'B7' || sqr == 'B8') return 'SE'
    if (sqr == 'J4' || sqr == 'J5' || sqr == 'J6' || sqr == 'J7' || sqr == 'J8') return 'SE'
    if (sqr == 'D10' || sqr == 'E10' || sqr == 'F10' || sqr == 'G10' || sqr == 'H10') return 'SE'
    if (sqr == 'D2' || sqr == 'E2' || sqr == 'F2' || sqr == 'G2' || sqr == 'H2') return 'SE'

    //Black
    //                if (col == 7) return 'BP';
    //                if (sqr == 'H8' || sqr == 'H8') return 'BR';
    //                if (sqr == 'A8' || sqr == 'H8') return 'BR';
    //                if (sqr == 'B8' || sqr == 'G8') return 'BH';
    //                if (sqr == 'C8' || sqr == 'F8') return 'BB';
    //                if (sqr == 'D8') return 'BQ';
    //                if (sqr == 'E8') return 'BK';
    if (sqr == 'D5' || sqr == 'D6' || sqr == 'D7') return 'GE'
    if (sqr == 'H5' || sqr == 'H6' || sqr == 'H7') return 'GE'
    if (sqr == 'E8' || sqr == 'F8' || sqr == 'G8') return 'GE'
    if (sqr == 'E4' || sqr == 'F4' || sqr == 'G4') return 'GE'
    if (sqr == 'F6') return 'GFS'

  }

  $scope.moveGenerator = async () => {
    var genMove = new Object();
    genMove.remainingMoves = remainingMoves;
    genMove.player = AI;

    let headers = {
      headers: {
        "Content-Type": "application/json"
      }
    };


    $http.post("/api/generatedMove", genMove, headers).then((response) => {

      console.log(response);
      var from = response.data.From;
      var to = response.data.To;
      var initial = document.getElementsByClassName(from);
      var destination = document.getElementsByClassName(to);

      var moveObj = new Object();
      moveObj.from = initial[0].className.split(' ')[2];
      moveObj.to = destination[0].className.split(' ')[2];
      moveObj.player = initial[0].className.split(' ')[3].toString().charAt(0);
      moveObj.remainingMoves = remainingMoves;


      let headers = {
        headers: {
          "Content-Type": "application/json"
        }
      };


      $http.post("/api/move", moveObj, headers).then((response) => {
        console.log(response);
      });
      var piece = initial[0].className.split(' ')[3];

      destination[0].classList.add(initial[0].className.split(' ')[3]);
      initial[0].classList.remove(piece);
      logs[count] = 'Turn ' + turnCount + '| ' + playersTurn + ' moves ' +
      moveObj.from + ' to ' + moveObj.to;
      count++;
      if ((from.row == to.row - 1) && (from.col == to.col - 1) ||
        (from.row == to.row - 1) && (from.col == to.col + 1) ||
        (from.row == to.row + 1) && (from.col == to.col - 1) ||
        (from.row == to.row + 1) && (from.col == to.col + 1)) {
        console.log("capture");
        remainingMoves = remainingMoves - 2;
      } else if (piece == "GFS" && playersTurn == "G") {
        remainingMoves = remainingMoves - 2;
        console.log(remainingMoves);

      } else {
        console.log("remaining " + remainingMoves);
        remainingMoves--;
        console.log("123 Move");
        console.log("TEST " + remainingMoves);
      }

    });
  }

  setInterval(async function () {
    console.log(remainingMoves);
    //       console.log("playersTurn " + playersTurn)
    if (playersTurn == AI && remainingMoves > 0) {
      await $scope.moveGenerator();
      setTimeout(() => {

        //                remainingMoves--;
        if (remainingMoves == 0) {
          turnCount++;
          console.log("Swap players");
          if (playersTurn == "G") {
            console.log("Silver turn");
            playersTurn = "S";
            $scope.$apply(() => {
              $scope.object = { turn: "S" };
              $scope.object.count = turnCount;
              $scope.object.logs = logs;
            });
            remainingMoves = 2;
          } else {
            console.log("Gold turn");
            playersTurn = "G";
            $scope.$apply(() => {
              $scope.object = { turn: "G" };
              $scope.object.count = turnCount;
              $scope.object.logs = logs;
            });
            remainingMoves = 2;
          }
        }
      }, 100);


    }
  }, 1000)

});

function letterToNumber(letter) {
  switch (letter) {
    case 'A':
      return 1;
      break;
    case 'B':
      return 2;
      break;
    case 'C':
      return 3;
      break;
    case 'D':
      return 4;
      break;
    case 'E':
      return 5;
      break;
    case 'F':
      return 6;
      break;
    case 'G':
      return 7;
      break;
    case 'H':
      return 8;
      break;
    case 'I':
      return 9;
      break;
    case 'J':
      return 10;
      break;
    case 'K':
      return 11;
      break;
    default:
      return 100;
      break;
  }

}