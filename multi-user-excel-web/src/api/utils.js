//求Array1中Array2没有的元素集合
export function minusArray (Array1,Array2) {
    return  Array1.filter(function (v) {
        return Array2.indexOf(v) === -1
      })
  }