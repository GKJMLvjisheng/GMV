
const randomNum = (min,max) => {
  let Range = max - min;
      let Rand = Math.random();
      let num = min + Math.round(Rand * Range); //四舍五入
      return num;
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
  randomNum,
  createPositionArr,
  getArrItems
}