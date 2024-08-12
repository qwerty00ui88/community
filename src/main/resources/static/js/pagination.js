class Paginator {
    constructor(loadDataCallback, renderCallback, pageSize = 10) {
        this.pageSize = pageSize;
        this.currentPage = 0;
        this.loadDataCallback = loadDataCallback; // 데이터를 가져오는 함수
        this.renderCallback = renderCallback; // 데이터를 렌더링하는 함수
    }

    loadPage(page) {
        this.currentPage = page;

        this.loadDataCallback({
            page: page,
            size: this.pageSize
        }).then(response => {
            this.renderCallback(response.data);
            this.updatePagination(response.data.pagination);
        }).catch(error => {
            console.log("Error: " + error);
        });
    }

    updatePagination(pagination) {
        let paginationContainer = $('.pagination');
        paginationContainer.empty();

        if (pagination.totalPages > 1) {
            let paginationHtml = `
                <li class="page-item ${pagination.currentPage == 0 ? 'disabled' : ''}">
                    <a class="page-link pagination-link" data-page="0"><<</a>
                </li>
                <li class="page-item ${pagination.currentPage == 0 ? 'disabled' : ''}">
                    <a class="page-link pagination-link" data-page="${pagination.currentPage - 1}"><</a>
                </li>`;

            for (let i = pagination.startPage; i <= pagination.endPage; i++) {
                paginationHtml += `
                    <li class="page-item ${i == pagination.currentPage ? 'active' : ''}">
                        <a class="page-link pagination-link" data-page="${i}">${i + 1}</a>
                    </li>`;
            }

            paginationHtml += `
                <li class="page-item ${pagination.currentPage == pagination.totalPages - 1 ? 'disabled' : ''}">
                    <a class="page-link pagination-link" data-page="${pagination.currentPage + 1}">></a>
                </li>
                <li class="page-item ${pagination.currentPage == pagination.totalPages - 1 ? 'disabled' : ''}">
                    <a class="page-link pagination-link" data-page="${pagination.totalPages - 1}">>></a>
                </li>`;
            paginationContainer.append(paginationHtml);

            // 이벤트 리스너 추가
            paginationContainer.find('.pagination-link').on('click', (e) => {
                e.preventDefault();
                let selectedPage = $(e.target).data('page');
                this.loadPage(selectedPage);
            });
        }
    }

    setPageSize(pageSize) {
        this.pageSize = pageSize;
        this.loadPage(0); // 페이지 크기 변경 시 첫 페이지부터 로드
    }
}







