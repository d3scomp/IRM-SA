function data = loadData(varargin)
%% LOADDATA reads specified file containing comma separated values (csv).
% The first line is expected to contain a header, labeling each column of
% the data. A map indexed by header labels is returned.
%
% data = LOADDATA('fileName.csv')
%
% An augmented version of this command can be used to filter a specifit
% range of the data:
%
% data = LOADDATA('fileName.csv', 'range', 1:1000);
%
% or to quick plot some of the data:
% 
% data = LOADDATA('fileName.csv', 'plot', {'actual_temperature',
% 'belief_temperature'})

    [fileName, arguments] = processArguments(varargin{:});
    
    data = extractData(fileName);

    if isKey(arguments, rangeArg)
        data = filterData(data, arguments(rangeArg));
    end
    
    if isKey(arguments, plotArg)
        quickPlot(data, arguments(plotArg));
    end

end

function data = extractData(fileName)
%% EXTRACTDATA reads the specified file and returnes the data in a map
% indexed by the labels from the first line in the file.

    % Delimiter of individual data entries
    separator = ';';
    
    % Explicit dimensionality of data (implicit dimensionality is 1, but
    % e.g. position has 2 dimensions (x and y).
    explicitDimensionalities = containers.Map( ...
        {'actual_position', 'belief_position'}, ...
        [2, 2]);
    data = containers.Map();

    f = fopen(fileName);
    fprintf('Opening file "%s".\n', fileName);

    % Obtain the data keys
    lineCnt = lineCount(f);
    line = fgetl(f);
    lineNum = 1;
    keys = strsplit(line, separator);
    for k = 1:size(keys, 2)
        key = char(keys(k));
        fprintf('The following key found: "%s".\n', key);
        if isKey(explicitDimensionalities, key)
            % If the dimension of the data is specified, use it
            dimension = explicitDimensionalities(key);
            data(key) = zeros(dimension, lineCnt-1); % -1 to exclude the header
        else
            % Otherwise scalar values are expected
            data(key) = zeros(1, lineCnt-1); % -1 to exclude the header
        end
    end

    % Load all the values
    fprintf('Processing the data ...\n');
    line = fgetl(f);
    lineNum = lineNum + 1;
    while ischar(line)
        % Process the line
        values = strsplit(line, separator);
        if size(values, 2) ~= size(keys, 2)
            fprintf('Line %d is incomplete. Skipping the line.\n', lineNum);
        else
            for i = 1:size(keys, 2)
                % Process each column
                key = char(keys(i));
                v = data(key);
                value = str2num(values{i}); %#ok<ST2NM>
                v(:, lineNum-1) = value; % -1 to skip the header
                data(key) = v;
            end
        end
        line = fgetl(f);
        lineNum = lineNum + 1;
    end
    
    fclose(f);
    fprintf('Reading the file completed.\n');
end

function filtered = filterData(data, range)
%% FILTERDATA filters the wanted data.

    keySet = keys(data);
    filtered = containers.Map();
    for i = 1:size(keySet, 2)
        % Process each column
        key = char(keySet(i));
        v = data(key);
        filtered(key) = v(range);
    end
end

function quickPlot(data, selected)
%% QUICKPLOT simply visualizes the specified data in a line plot.

    time = 'time';
    if ~isKey(data, time)
        fprintf('Data for "%s" not found.\n', time);
        return;
    end
    
    timeInSec = data(time)/1000;
    for j = 1:size(selected, 2)
        key = char(selected(j));
        if isKey(data, key)
            fprintf('Plotting "%s".\n', key);
            figure('name', key);
            plot(timeInSec, data(key));
        else
            fprintf('"%s" data not found. Can''t plot them.\n', key);
        end
    end
end

function cnt = lineCount(fid)
%% LINECOUNT provides the number of lines in the specified file passed
% as handle to an open file. After the lilne count is determined the handle
% is rewinded to the beginning of the file.

    n = 0;
    line = fgetl(fid);
    while ischar(line)
        n = n + 1;
        line = fgetl(fid);
    end
    
    frewind(fid);
    cnt = n;
end

function [fileName, arguments] = processArguments(varargin)
%% PROCESSARGUMENTS checks correctness of provided arguments and parses them
% into a map of correctly typed values.

    if(nargin < 1)
        error('No file name specified');
    else
        fileName = char(varargin{1});
    end
    arguments = containers.Map();
    if(nargin > 1)
        for i = 2:2:nargin
            key = char(varargin{i});
            if nargin < i+1
                error('No value specified for the argument "%s"', key);
            end
            
            switch key
                case rangeArg
                    if isnumeric(varargin{i+1})
                        arguments(rangeArg) = varargin{i+1};
                    else
                        error('Invalid value of "%s" argument', rangeArg);
                    end
                case plotArg
                    if iscellstr(varargin{i+1})
                        arguments(plotArg) = varargin{i+1};
                    else
                        error('Invalid value of "%s" argument', plotArg);
                    end
                otherwise
                    error('Unknown argument "%s"', key);
            end
        end
    end
end

%% Names of supported options
function a = plotArg()
% PLOTARG represents the name of the 'plot' option.

    a = 'plot';
end

function a = rangeArg()
% RANGEARG represents the name of the 'range' option.

    a = 'range';
end