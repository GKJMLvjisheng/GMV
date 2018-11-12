
const randomNum = (min,max) => {
  let Range = max - min;
      let Rand = Math.random();
      let num = min + Math.round(Rand * Range); //四舍五入
      return num;
}
const numAdd = (num1, num2) => {
  var baseNum, baseNum1, baseNum2;
  try {
      baseNum1 = num1.toString().split(".")[1].length;
  } catch (e) {
      baseNum1 = 0;
  }
  try {
      baseNum2 = num2.toString().split(".")[1].length;
  } catch (e) {
      baseNum2 = 0;
  }
  baseNum = Math.pow(10, Math.max(baseNum1, baseNum2));
  return (num1 * baseNum + num2 * baseNum) / baseNum;
}
const createPositionArr = () => {
  let xArr = [20,95,170,245,330,395,470,595,675]
  let yArr = [20,140,260,380,500]
  let pArr = []
  xArr.forEach(xEl => {
    yArr.forEach(yEl => {
      pArr.push({x: xEl, y: yEl})
    })
  })
  return pArr
}

const getArrItems = (arr, count) => {
  var tempArr  = []
  var returnArr = []
  for (let i in arr) {
    tempArr.push(arr[i])
  }
  for (let i = 0; i < count; i++) {
    if(tempArr.length > 0) {
      var randomIdx = randomNum(0, tempArr.length)
      returnArr.push(tempArr[randomIdx])
      tempArr.splice(randomIdx, 1) 
    } else {
      break
    }
  }
  return returnArr
}
export {
  numAdd,
  randomNum,
  createPositionArr,
  getArrItems
}