export const debounce = (func, wait) => {
  let timeout;
  return function executedFunction(...args) {
    const later = () => {
      clearTimeout(timeout);
      func(...args);
    };
    clearTimeout(timeout);
    timeout = setTimeout(later, wait);
  };
};

export const throttle = (func, wait) => {
  let timeout;
  let previous = 0;
  return function executedFunction(...args) {
    const now = Date.now();
    if (now - previous > wait) {
      func.apply(this, args);
      previous = now;
    } else {
      clearTimeout(timeout);
      timeout = setTimeout(() => {
        previous = now;
        func.apply(this, args);
      }, wait - (now - previous));
    }
  };
};
