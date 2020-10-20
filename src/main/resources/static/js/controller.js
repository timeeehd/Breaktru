const app = angular.module('myApp', ['toastr']);

app.controller('BreaktruController', (toastr, $scope, $http) => {
  $scope.rows = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K'];
  $scope.columns = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11];

  let goldSide = "";
  let silverSide = "";
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
  let rememberMoves = null;
  let remainingTime = 600000;

  let prevMoves = [];

  var pos = [];

  //Toggle of selected / destination markers on board
  $scope.getInitial = function (col, row, idx, idxp) {

    pos.push(idx, idxp);

    //ng-class '.initial' or '.destination' applied to selected element
    $scope.initialIdx = [pos[0], pos[1]];
    $scope.destinationIdx = [pos[2], pos[3]];

    if (pos.length >= 4) pos.splice(0);

    $scope.move = function () {
      var initial = angular.element('.initial');
      var destination = angular.element('.destination');

      // Piece class
      var piece = initial[0].className.split(' ')[3];
      var position = initial[0].className.split(' ')[2];
      var positionTo = destination[0].className.split(' ')[2];

      var checkPos = destination[0].className.split(' ');

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
        if (response.data.result == ("GAME WON by " + moveObj.player)) {
          remainingMoves = -100;
          toastr.success("GAME WON by " + moveObj.player, { timeOut: 1000000 })
        }
        if (response.data.result == ("ILLEGAL MOVE")) {
          remainingMoves = -100;
          toastr.error("ILLEGAL MOVE BY: " + moveObj.player, { timeOut: 1000000 });
        }
      });

      var blkWht = checkPos[3].substring(0, 1);
      if (prevMoves.length == 2) {
        prevMoves.shift();
      }
      //check if position is white or black piece already
      if (blkWht === 'S' || blkWht === 'G') {
        //remove the overtaken piece class
        var pieceTo = checkPos[checkPos.length - 2].substring(0, checkPos[checkPos.length - 2].length);
        prevMoves.push({
          "piece": piece,
          "position": position,
          "pieceTo": pieceTo,
          "postitionTo": positionTo
        });

        destination.removeClass(checkPos[checkPos.length - 2].substring(0, checkPos[checkPos.length - 2].length));
      } else {
        prevMoves.push({
          "piece": piece,
          "position": position,
          "postitionTo": positionTo
        });
      }
      //Add class to overtaken position
      destination.addClass(initial[0].className.split(' ')[3]);

      //reset square
      initial.removeClass(piece);

      //reset ng-class values
      this.initialIdx = false;
      this.destinationIdx = false;
      var from = new Object();
      from.col = letterToNumber(initial[0].className.split(' ')[2].toString().charAt(0));
      from.row = parseInt(initial[0].className.split(' ')[2].toString().substring(1));
      var to = new Object();
      to.col = letterToNumber(destination[0].className.split(' ')[2].toString().charAt(0));
      to.row = parseInt(destination[0].className.split(' ')[2].toString().substring(1));

      if (((from.row == to.row - 1) && (from.col == to.col - 1)) ||
        ((from.row == to.row - 1) && (from.col == to.col + 1)) ||
        ((from.row == to.row + 1) && (from.col == to.col - 1)) ||
        ((from.row == to.row + 1) && (from.col == to.col + 1))) {
        logs[count] = 'Turn ' + turnCount + '| ' + playersTurn + ' moves ' +
          moveObj.from + ' to ' + moveObj.to + ' Capture';
        remainingMoves = remainingMoves - 2;
      } else if (piece == "GFS" && playersTurn == "G") {
        remainingMoves = remainingMoves - 2;
        logs[count] = 'Turn ' + turnCount + '| ' + playersTurn + ' moves ' +
          moveObj.from + ' to ' + moveObj.to + ' FlagShip';

      } else {
        logs[count] = 'Turn ' + turnCount + '| ' + playersTurn + ' moves ' +
          moveObj.from + ' to ' + moveObj.to;
        rememberMoves = remainingMoves;
        remainingMoves--;
      }
      $scope.object.logs = logs;
      if (remainingMoves == 0) {
        turnCount++;
        if (playersTurn == "G") {
          playersTurn = "S";
          $scope.object = { turn: "S" };
          $scope.object.count = turnCount;
          $scope.object.logs = logs;
          remainingMoves = 2;
          $scope.object.remainingTime = Math.floor(remainingTime / 1000);
        } else {
          playersTurn = "G";
          $scope.object = { turn: "G" };
          $scope.object.count = turnCount;
          $scope.object.logs = logs;
          remainingMoves = 2;
          $scope.object.remainingTime = Math.floor(remainingTime / 1000);
        }
      }
      count++;
    }
  }

  $scope.undo = () => {
    let headers = {
      headers: {
        "Content-Type": "application/json"
      }
    };

    $http.post("/api/undo", headers).then((response) => { });

    var lastMove = prevMoves[prevMoves.length - 1];
    var size = Object.keys(lastMove).length;
    if (size == 3) {
      var oldPos = document.getElementsByClassName(lastMove["position"]);
      oldPos[0].classList.add(lastMove["piece"]);
      var newPost = document.getElementsByClassName(lastMove["postitionTo"]);
      newPost[0].classList.remove(lastMove["piece"]);
      logs[count] = 'Undo ' + '| ' + ' moves ' +
        lastMove["position"] + ' to ' + lastMove["postitionTo"];
      if (lastMove["piece"] == "GFS") {
        playersTurn = "S";
        $scope.object = { turn: "S" };
        $scope.object.count = turnCount;
        $scope.object.logs = logs;
        remainingMoves = 2;
      } else if (playersTurn == "G") {
        if (rememberMoves == 1) {
          playersTurn = "S";
          $scope.object = { turn: "S" };
        }
        $scope.object.count = turnCount;
        $scope.object.logs = logs;
        remainingMoves = rememberMoves;
      } else {
        if (rememberMoves == 1) {
          playersTurn = "G";
          $scope.object = { turn: "G" };
        }
        $scope.object.count = turnCount;
        $scope.object.logs = logs;
        remainingMoves = rememberMoves;
      }
    } else {
      var oldPos = document.getElementsByClassName(lastMove["position"]);
      oldPos[0].classList.add(lastMove["piece"]);
      var newPost = document.getElementsByClassName(lastMove["postitionTo"]);
      newPost[0].classList.remove(lastMove["piece"]);
      newPost[0].classList.add(lastMove["pieceTo"]);
      logs[count] = 'Undo ' + '| ' + ' moves ' +
        lastMove["position"] + ' to ' + lastMove["postitionTo"] + " Capture";
      if (playersTurn == "G") {
        playersTurn = "S";
        $scope.object = { turn: "S" };
        $scope.object.count = turnCount;
        $scope.object.logs = logs;
        remainingMoves = 2;
      } else {
        playersTurn = "G";
        $scope.object = { turn: "G" };
        $scope.object.count = turnCount;
        $scope.object.logs = logs;
        remainingMoves = 2;
      }
    };
    if (prevMoves.length != 0) {
      prevMoves.pop();
    }
    count++;
  }

  $scope.init = function () {
    playersTurn = "G";
    turnCount = 0;
    remainingMoves = -1;
    logs = {};
    $scope.object.logs = {};
    $scope.object.count = null;
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
    goldSide = "Human";
    silverSide = "AI";
    remainingMoves = 2;
    $scope.goldPlayer = "Human";
    $scope.silverPlayer = "AI";
    $scope.object = { turn: "G" };
    $scope.object.count = 1;
    turnCount = 1;
    $scope.object.remainingTime = remainingTime / 1000;
  }

  $scope.silver = function () {
    goldSide = "AI";
    silverSide = "Human";
    remainingMoves = 2;
    $scope.goldPlayer = "AI";
    $scope.silverPlayer = "Human";
    $scope.object = { turn: "G" };
    $scope.object.count = 1;
    turnCount = 1;

    $scope.object.remainingTime = remainingTime / 1000;
  }

  $scope.twoAI = function () {
    goldSide = "AI";
    silverSide = "AI";
    remainingMoves = 2;
    $scope.goldPlayer = "AI";
    $scope.silverPlayer = "AI";
    $scope.object = { turn: "G" };
    $scope.object.count = 1;
    turnCount = 1;
    playersTurn = "G";
  }
  // This function is called inside of the ng-class object, for each div that is repeated,
  // It sets up the initial pieces classes for each side.
  $scope.piece = (row, col) => {
    var sqr = row + col;

    if (sqr == 'B4' || sqr == 'B5' || sqr == 'B6' || sqr == 'B7' || sqr == 'B8') return 'SE'
    if (sqr == 'J4' || sqr == 'J5' || sqr == 'J6' || sqr == 'J7' || sqr == 'J8') return 'SE'
    if (sqr == 'D10' || sqr == 'E10' || sqr == 'F10' || sqr == 'G10' || sqr == 'H10') return 'SE'
    if (sqr == 'D2' || sqr == 'E2' || sqr == 'F2' || sqr == 'G2' || sqr == 'H2') return 'SE'

    if (sqr == 'D5' || sqr == 'D6' || sqr == 'D7') return 'GE'
    if (sqr == 'H5' || sqr == 'H6' || sqr == 'H7') return 'GE'
    if (sqr == 'E8' || sqr == 'F8' || sqr == 'G8') return 'GE'
    if (sqr == 'E4' || sqr == 'F4' || sqr == 'G4') return 'GE'
    if (sqr == 'F6') return 'GFS'

  }

  $scope.moveGenerator = async () => {

    var genMove = new Object();
    genMove.remainingMoves = remainingMoves;
    genMove.player = playersTurn;

    let headers = {
      headers: {
        "Content-Type": "application/json"
      }
    };

    $http.post("/api/alphaBeta3", genMove, headers).then((response) => {
      var fromVar = response.data.From;
      var toVar = response.data.To;
      var initial = document.getElementsByClassName(fromVar);
      var destination = document.getElementsByClassName(toVar);

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
      remainingTime = remainingTime - response.data.time;

      $http.post("/api/move", moveObj, headers).then((response) => {
        if (response.data.result == ("GAME WON by " + moveObj.player)) {
          remainingMoves = -100;
          toastr.success("GAME WON by " + moveObj.player, { timeOut: 1000000 })
        }
        if (response.data.result == ("ILLEGAL MOVE")) {
          remainingMoves = -100;
          toastr.error("ILLEGAL MOVE BY: " + moveObj.player, { timeOut: 1000000 });
        }
      });
      var piece = initial[0].className.split(' ')[3];
      var test8 = destination[0];
      var checkPos = destination[0].className.split(' ');
      var position = initial[0].className.split(' ')[2];
      var positionTo = destination[0].className.split(' ')[2];
      if (prevMoves.length == 2) {
        prevMoves.shift();
      }
      if (checkPos.length == 4) {
        var blkWht = checkPos[3].substring(0, 1);
        //check if position is gold or silver piece already
        if (blkWht === 'S' || blkWht === 'G') {
          var pieceTo = checkPos[checkPos.length - 1].substring(0, checkPos[checkPos.length - 1].length);
          //remove the overtaken piece class
          prevMoves.push({
            "piece": piece,
            "position": position,
            "pieceTo": pieceTo,
            "postitionTo": positionTo
          });
          destination[0].classList.remove(checkPos[checkPos.length - 1].substring(0, checkPos[checkPos.length - 1].length))
          destination = document.getElementsByClassName(toVar);
        }
      } else {
        prevMoves.push({
          "piece": piece,
          "position": position,
          "postitionTo": positionTo
        });
      }
      destination[0].classList.add(initial[0].className.split(' ')[3]);
      initial[0].classList.remove(piece);
      var from = {};
      var to = {};
      from["row"] = parseInt(fromVar.substring(1));
      from["col"] = letterToNumber(fromVar.substring(0, 1));
      to["row"] = parseInt(toVar.substring(1));
      to["col"] = letterToNumber(toVar.substring(0, 1));
      if ((from["row"] == to["row"] - 1) && (from["col"] == to["col"] - 1)) {
        remainingMoves = remainingMoves - 2;
        logs[count] = 'Turn ' + turnCount + '| ' + playersTurn + ' moves ' +
          moveObj.from + ' to ' + moveObj.to + '       Capture';
      } else if ((from["row"] == to["row"] - 1) && (from["col"] == to["col"] + 1)) {
        remainingMoves = remainingMoves - 2;
        logs[count] = 'Turn ' + turnCount + '| ' + playersTurn + ' moves ' +
          moveObj.from + ' to ' + moveObj.to + '       Capture';
      } else if ((from["row"] == to["row"] + 1) && (from["col"] == to["col"] - 1)) {
        remainingMoves = remainingMoves - 2;
        logs[count] = 'Turn ' + turnCount + '| ' + playersTurn + ' moves ' +
          moveObj.from + ' to ' + moveObj.to + '       Capture';
      } else if ((from["row"] == to["row"] + 1) && (from["col"] == to["col"] + 1)) {
        remainingMoves = remainingMoves - 2;
        logs[count] = 'Turn ' + turnCount + '| ' + playersTurn + ' moves ' +
          moveObj.from + ' to ' + moveObj.to + '       Capture';
      } else if (piece == "GFS" && playersTurn == "G") {
        remainingMoves = remainingMoves - 2;
        logs[count] = 'Turn ' + turnCount + '| ' + playersTurn + ' moves ' +
          moveObj.from + ' to ' + moveObj.to + '       Flagship';
      } else {
        rememberMoves = remainingMoves;
        logs[count] = 'Turn ' + turnCount + '| ' + playersTurn + ' moves ' +
          moveObj.from + ' to ' + moveObj.to;
        remainingMoves--;
      }
      setTimeout(() => {
        if (remainingMoves == 0) {
          turnCount++;
          if (playersTurn == "G") {
            playersTurn = "S";
            $scope.$apply(() => {
              $scope.object = { turn: "S" };
              $scope.object.count = turnCount;
              $scope.object.logs = logs;
              $scope.object.remainingTime = Math.floor(remainingTime / 1000);
            });
            remainingMoves = 2;
          } else {
            playersTurn = "G";
            $scope.$apply(() => {
              $scope.object = { turn: "G" };
              $scope.object.count = turnCount;
              $scope.object.logs = logs;
              $scope.object.remainingTime = Math.floor(remainingTime / 1000);
            });
            remainingMoves = 2;
          }
        }
      }, 10);
      $scope.object.remainingTime = Math.floor(remainingTime / 1000);
      count++;
    });
  }

  $scope.pause = () => {
    rememberMoves = remainingMoves;
    remainingMoves = -1;
  }

  $scope.pass = () => {
    remainingMoves = 2;
    logs[count] = 'Turn ' + turnCount + '| ' + playersTurn + ' move: PASS ';
    turnCount = turnCount + 1;
    $scope.object.count = turnCount;
    $scope.object.logs = logs;
    if (playersTurn == "G") {
      $scope.object = { turn: "S" };
      playersTurn = "S";
    } else if (playersTurn == "S") {
      $scope.object = { turn: "G" };
      playersTurn = "G";
    }

    count++;
  }

  $scope.startAgain = async () => {
    $scope.moveGenerator()
  }

  $scope.download = function () {
    var file = new Blob([JSON.stringify(logs)], { type: 'application/json' });
    let currentTime = Date.now();

    if (window.navigator.msSaveOrOpenBlob) // IE10+
      window.navigator.msSaveOrOpenBlob(file, "logFile-G" + goldSide + "-S" + silverSide + "-" + currentTime + ".txt");
    else { // Others
      var a = document.createElement("a"),
        url = URL.createObjectURL(file);
      a.href = url;
      a.download = "logFile-G" + goldSide + "-S" + silverSide + "-" + currentTime + ".txt";
      document.body.appendChild(a);
      a.click();
      setTimeout(function () {
        document.body.removeChild(a);
        window.URL.revokeObjectURL(url);
      }, 0);
    }
  }

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