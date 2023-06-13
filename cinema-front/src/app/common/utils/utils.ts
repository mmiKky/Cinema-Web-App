/**
 * @Returns a date in YYYY-MM-DD format
 */
export function today() {
  return new Date().toISOString().split('T')[0];
}

export function dateToString(date: Date) {
  return date.toISOString().split('T')[0];
}
