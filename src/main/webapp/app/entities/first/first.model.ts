import { BaseEntity } from './../../shared';

export class First implements BaseEntity {
    constructor(
        public id?: number,
        public no?: number,
        public name?: string,
    ) {
    }
}
