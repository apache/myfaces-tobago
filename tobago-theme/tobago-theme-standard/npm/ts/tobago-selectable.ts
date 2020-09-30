export enum Selectable {
  none, // Not selectable.
  multi, // Multi selection possible. No other limitations.
  single, // Only one item is selectable.
  singleOrNone, // Only one of no item is selectable.
  multiLeafOnly, // Only leafs are selectable.
  singleLeafOnly, // Only one item is selectable and it must be a leaf.
  sibling, // Only siblings are selectable.
  siblingLeafOnly, // Only siblings are selectable and they have to be leafs.
  multiCascade // Multi selection possible. When (de)selecting an item, the subtree will also be (un)selected.
}
